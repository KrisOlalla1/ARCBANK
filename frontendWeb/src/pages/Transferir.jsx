import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { realizarTransferencia } from '../services/bancaApi'
import { useNavigate } from "react-router-dom";

export default function Transfer() {
    const { state, addTransaction } = useAuth();
    const navigate = useNavigate();

    const [step, setStep] = useState(1);

    const [toAccount, setToAccount] = useState("");
    const [bank, setBank] = useState("");
    const [toName, setToName] = useState("");

    const [fromAcc, setFromAcc] = useState(state.user.accounts[0].id);
    const fromAccount = state.user.accounts.find(a => a.id === fromAcc);

    const [amount, setAmount] = useState("");
    const [error, setError] = useState("");

    const goToStep2 = () => {
    if (!toAccount || !bank || !toName)
        return setError("Todos los campos son obligatorios.");

    if (toAccount.length !== 12)
        return setError("El número de cuenta debe tener exactamente 12 dígitos.");

    setError("");
    setStep(2);
};


    const goToStep3 = () => {
        const num = Number(amount);
        if (!num || num <= 0) return setError("Monto inválido.");
        if (num > fromAccount.balance)
            return setError("Saldo insuficiente en la cuenta.");
        setError("");
        setStep(3);
    };

    const confirmTransfer = () => {
                const idUsuarioWeb = state.user.idUsuarioWeb;
                if (!idUsuarioWeb) {
                    return setError('No se pudo identificar el usuario. Por favor, inicie sesión nuevamente.');
                }

                const request = {
                        idUsuarioWeb: idUsuarioWeb,
                        cuentaOrigen: fromAccount.number,
                        cuentaDestino: toAccount,
                        monto: Number(amount),
                        descripcion: `Transferencia a ${toName} (Banco ${bank})`
                }

                realizarTransferencia(request)
                    .then(res => {
                        const tx = {
                            id: "tx-" + Date.now(),
                            accId: fromAcc,
                            date: new Date().toISOString().slice(0, 10),
                            desc: "Transferencia directa",
                            name: toName,
                            amount: -Number(amount),
                        };
                        addTransaction(tx);
                        setStep(4);
                        setTimeout(() => {
                            navigate('/movimientos');
                        }, 2000);
                    })
                    .catch(err => {
                        alert(err.body || err.message || 'Error en la transferencia')
                    })
    };

    const downloadReceipt = () => {
        const text =
            `TRANSFERENCIA EXITOSA\n\n` +
            `Monto: $${Number(amount).toFixed(2)}\n` +
            `Desde cuenta: ${fromAccount.number}\n` +
            `A nombre de: ${toName}\n` +
            `Cuenta destino: ${toAccount}\n` +
            `Banco: ${bank}\n` +
            `Fecha: ${new Date().toLocaleString()}\n`;

        const blob = new Blob([text], { type: "text/plain" });
        const url = URL.createObjectURL(blob);

        const a = document.createElement("a");
        a.href = url;
        a.download = "comprobante.txt";
        a.click();
        URL.revokeObjectURL(url);
    };

    return (
        <div style={{ padding: 30 }}>
            {step === 1 && (
    <div style={styles.card}>
        <h2 style={styles.title}>Transferir a</h2>

        <label>N° de cuenta</label>
        <input
            style={styles.input}
            value={toAccount}
            maxLength={12}
            onChange={(e) => {
                const val = e.target.value.replace(/\D/g, ""); 
                setToAccount(val);
            }}
        />

        <label>Banco</label>
        <input
            style={styles.input}
            value={bank}
            onChange={e => setBank(e.target.value)}
        />

        <label>Nombres</label>
        <input
            style={styles.input}
            value={toName}
            onChange={e => setToName(e.target.value)}
        />

        {error && <p style={styles.error}>{error}</p>}

        <button style={styles.btn} onClick={goToStep2}>
            Continuar
        </button>
    </div>
)}


            {step === 2 && (
                <div style={styles.card}>
                    <h2 style={styles.title}>Transferir</h2>

                    <div style={styles.balanceCircle}>
                        <div style={styles.circleLetter}>
                            {fromAccount.id[0]}
                        </div>
                        <div style={{ fontSize: 20, fontWeight: 700 }}>{fromAccount.id}</div>
                        <div style={{ fontSize: 26, marginTop: 5 }}>
                            ${fromAccount.balance.toFixed(2)}
                        </div>
                    </div>

                    <label style={{ fontWeight: 600 }}>Desde la cuenta</label>
                    <select
                        style={styles.input}
                        value={fromAcc}
                        onChange={(e) => setFromAcc(e.target.value)}
                    >
                        {state.user.accounts.map(acc => (
                            <option key={acc.id} value={acc.id}>
                                {acc.id} — Saldo ${acc.balance.toFixed(2)}
                            </option>
                        ))}
                    </select>

                    <label>Monto a transferir</label>
                    <input
                        style={styles.input}
                        value={amount}
                        onChange={e => setAmount(e.target.value)}
                        placeholder="0.00"
                    />

                    <div style={styles.infoBar}>
                        💡 Esta transacción no tiene costo
                    </div>

                    {error && <p style={styles.error}>{error}</p>}

                    <button style={styles.btn} onClick={goToStep3}>
                        Continuar
                    </button>
                </div>
            )}


            {step === 3 && (
                <div style={styles.card}>
                    <h2 style={styles.title}>Resumen</h2>

                    <h3 style={{ textAlign: "center", marginBottom: 15 }}>
                        Transferencia directa
                    </h3>

                    <table style={styles.table}>
                        <tbody>
                            <tr>
                                <td>Monto a transferir:</td>
                                <td><b>${Number(amount).toFixed(2)}</b></td>
                            </tr>
                            <tr>
                                <td>Costo de transacción:</td>
                                <td>$0.00</td>
                            </tr>

                            <tr>
                                <td colSpan={2} style={styles.sectionTitle}>Cuenta de origen</td>
                            </tr>
                            <tr>
                                <td>Número:</td>
                                <td>{fromAccount.number}</td>
                            </tr>

                            <tr>
                                <td colSpan={2} style={styles.sectionTitle}>Cuenta de destino</td>
                            </tr>
                            <tr>
                                <td>Nombres:</td>
                                <td>{toName}</td>
                            </tr>
                            <tr>
                                <td>N° de cuenta:</td>
                                <td>{toAccount}</td>
                            </tr>
                            <tr>
                                <td>Banco:</td>
                                <td>{bank}</td>
                            </tr>
                        </tbody>
                    </table>

                    <div style={styles.buttonRow}>
                        <button style={styles.btnCancel} onClick={() => setStep(2)}>
                            Cancelar
                        </button>
                        <button style={styles.btn} onClick={confirmTransfer}>
                            Confirmar
                        </button>
                    </div>
                </div>
            )}

            {step === 4 && (
                <div style={styles.card}>
                    <h2 style={styles.title}>Resumen</h2>

                    <h3 style={{ textAlign: "center", marginBottom: 20 }}>
                        ✔ Transferencia exitosa
                    </h3>

                    <table style={styles.table}>
                        <tbody>
                            <tr>
                                <td>Monto:</td>
                                <td><b>${Number(amount).toFixed(2)}</b></td>
                            </tr>

                            <tr>
                                <td colSpan={2} style={styles.sectionTitle}>Cuenta de origen</td>
                            </tr>
                            <tr>
                                <td>Número:</td>
                                <td>{fromAccount.number}</td>
                            </tr>

                            <tr>
                                <td colSpan={2} style={styles.sectionTitle}>Cuenta de destino</td>
                            </tr>
                            <tr>
                                <td>Nombres:</td>
                                <td>{toName}</td>
                            </tr>
                            <tr>
                                <td>N° de cuenta:</td>
                                <td>{toAccount}</td>
                            </tr>
                            <tr>
                                <td>Banco:</td>
                                <td>{bank}</td>
                            </tr>
                        </tbody>
                    </table>

                    <div style={styles.buttonRow}>
                        <button style={styles.btn} onClick={() => navigate("/")}>
                            ⬅ Inicio
                        </button>
                        <button style={styles.btn} onClick={downloadReceipt}>
                            📄 Descargar
                        </button>
                    </div>

                </div>
            )}
        </div>
    );
}

const styles = {
    card: {
        background: "#fff",
        padding: 30,
        borderRadius: 10,
        width: "70%",
    },
    title: {
        fontSize: 22,
        marginBottom: 20,
        fontWeight: 700,
    },
    input: {
        width: "100%",
        padding: 10,
        borderRadius: 5,
        border: "1px solid #ccc",
        marginBottom: 20,
    },
    btn: {
        background: "#cc8c00",
        color: "white",
        padding: "10px 20px",
        borderRadius: 5,
        border: "none",
        cursor: "pointer",
        marginTop: 10,
    },
    btnCancel: {
        background: "#ddd",
        padding: "10px 20px",
        borderRadius: 5,
        border: "none",
        cursor: "pointer",
    },
    error: {
        color: "red",
        marginBottom: 10,
    },
    balanceCircle: {
        textAlign: "center",
        marginBottom: 20,
    },
    circleLetter: {
        width: 80,
        height: 80,
        borderRadius: "50%",
        background: "#c7d8ff",
        margin: "0 auto 10px",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontSize: 30,
        fontWeight: "bold",
    },
    infoBar: {
        background: "#e5f1ff",
        padding: 10,
        borderRadius: 5,
        marginBottom: 15,
        fontSize: 14,
    },
    table: {
        width: "70%",
        fontSize: 14,
        margin: "0 auto 20px",
        textAlign: "left",
    },
    sectionTitle: {
        fontWeight: 700,
        marginTop: 10,
        paddingTop: 10,
    },
    buttonRow: {
        display: "flex",
        justifyContent: "space-between",
        marginTop: 20,
    },
};

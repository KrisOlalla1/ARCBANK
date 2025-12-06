import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { realizarTransferenciaInterbancaria, getBancos } from '../services/bancaApi'
import { useEffect } from 'react'
import { useNavigate } from "react-router-dom";

export default function TransaccionesInterbancarias() {
    const { state, addTransaction } = useAuth();
    const navigate = useNavigate();

    const [step, setStep] = useState(1);

    const [toAccount, setToAccount] = useState("");
    const [bankName, setBankName] = useState("");
    const [banks, setBanks] = useState([])
    const [toName, setToName] = useState("");

    const defaultAccounts = state?.user?.accounts && state.user.accounts.length ? state.user.accounts : [{ id: 'DEV-001', number: '000000000001', balance: 1000 }];
    const firstAccId = defaultAccounts[0]?.id || '';
    const [fromAcc, setFromAcc] = useState(firstAccId);
    const fromAccount = (defaultAccounts || []).find(a => a.id === fromAcc) || { id: '', number: '', balance: 0 };

    const [amount, setAmount] = useState("");
    const [error, setError] = useState("");

    useEffect(()=>{
        let cancelled = false
        getBancos().then(res => {
            const list = (res && res.bancos) ? res.bancos : (Array.isArray(res) ? res : [])
            if (!cancelled) setBanks(list)
        }).catch(()=>{
            // ignore, keep banks empty and allow manual entry fallback
        })
        return ()=>{ cancelled = true }
    }, [])

    const goToStep2 = () => {
        if (!toAccount || !bankName || !toName)
            return setError("Todos los campos son obligatorios.");

        if (toAccount.replace(/\D/g, '').length < 6)
            return setError("El número de cuenta parece inválido.");

        setError("");
        setStep(2);
    };

    const goToStep3 = () => {
        const num = Number(amount);
        if (!num || num <= 0) return setError("Monto inválido.");
        if (num > (fromAccount.balance || 0))
            return setError("Saldo insuficiente en la cuenta.");
        setError("");
        setStep(3);
    };

    const confirmTransfer = () => {
                const idUsuarioWeb = state?.user?.idUsuarioWeb;
                if (!idUsuarioWeb) {
                    return setError('No se pudo identificar el usuario. Por favor, inicie sesión nuevamente.');
                }

                const request = {
                        idUsuarioWeb: idUsuarioWeb,
                    cuentaOrigen: fromAccount.number || '',
                        cuentaDestino: toAccount,
                        monto: Number(amount),
                        descripcion: `Transferencia interbancaria a ${toName} (Banco ${bankName})`,
                        bancoDestino: bankName
                }

                realizarTransferenciaInterbancaria(request)
                    .then(res => {
                        const tx = {
                            id: "tx-ib-" + Date.now(),
                            accId: fromAcc,
                            date: new Date().toISOString().slice(0, 10),
                            desc: "Transferencia interbancaria",
                            name: toName,
                            amount: -Number(amount),
                        };
                        if (typeof addTransaction === 'function') addTransaction(tx);
                        setStep(4);
                        setTimeout(() => {
                            navigate('/movimientos');
                        }, 2000);
                    })
                    .catch(err => {
                        alert(err.body || err.message || 'Error en la transferencia interbancaria')
                    })
    };

    const downloadReceipt = () => {
        const text =
            `TRANSFERENCIA INTERBANCARIA EXITOSA\n\n` +
            `Monto: $${Number(amount).toFixed(2)}\n` +
            `Desde cuenta: ${fromAccount.number}\n` +
            `A nombre de: ${toName}\n` +
            `Cuenta destino: ${toAccount}\n` +
            `Banco destino: ${bankName}\n` +
            `Fecha: ${new Date().toLocaleString()}\n`;

        const blob = new Blob([text], { type: "text/plain" });
        const url = URL.createObjectURL(blob);

        const a = document.createElement("a");
        a.href = url;
        a.download = "comprobante_interbancario.txt";
        a.click();
        URL.revokeObjectURL(url);
    };

    return (
        <div style={{ padding: 30 }}>
            {step === 1 && (
    <div style={styles.card}>
        <h2 style={styles.title}>Transferencia Interbancaria</h2>

        <label>N° de cuenta destino</label>
        <input
            style={styles.input}
            value={toAccount}
            onChange={(e) => setToAccount(e.target.value.replace(/\D/g, ''))}
            placeholder="Solo números"
        />

        <label>Banco</label>
        {banks && banks.length>0 ? (
            <select style={styles.input} value={bankName} onChange={e=>setBankName(e.target.value)}>
                <option value="">-- Seleccione un banco --</option>
                {banks.map((b, i) => (
                    <option key={b.id || i} value={b.nombre || b.name || b.id}>{b.nombre || b.name || b.id}</option>
                ))}
            </select>
        ) : (
            <input
                style={styles.input}
                value={bankName}
                onChange={e => setBankName(e.target.value)}
                placeholder="Ingrese el banco (fallback)"
            />
        )}

        <label>Nombres</label>
        <input
            style={styles.input}
            value={toName}
            onChange={e => setToName(e.target.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g,''))}
            placeholder="Solo letras"
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
                            {(fromAccount.id || '')[0] || 'U'}
                        </div>
                        <div style={{ fontSize: 20, fontWeight: 700 }}>{fromAccount.id || 'Cuenta'}</div>
                        <div style={{ fontSize: 26, marginTop: 5 }}>
                            ${Number(fromAccount.balance || 0).toFixed(2)}
                        </div>
                    </div>

                    <label style={{ fontWeight: 600 }}>Desde la cuenta</label>
                    <select
                        style={styles.input}
                        value={fromAcc}
                        onChange={(e) => setFromAcc(e.target.value)}
                    >
                        {(defaultAccounts || []).map(acc => (
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
                        💡 Esta transacción puede tardar más de 24 horas
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
                        Transferencia interbancaria
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
                                <td>{bankName}</td>
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
                        ✔ Transferencia interbancaria solicitada
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
                                <td>{bankName}</td>
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
        margin: "0 auto",
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

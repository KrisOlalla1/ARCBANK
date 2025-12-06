import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { realizarTransferencia } from '../services/bancaApi'
import { useNavigate } from "react-router-dom";

export default function Transfer() {
    const { state, addTransaction } = useAuth();
    const navigate = useNavigate();
    // Defensive handling: if `state.user` exists but `accounts` is missing or empty,
    // fall back to a development mock account so the UI can render.
    const accounts = (state && Array.isArray(state.user?.accounts) && state.user.accounts.length)
        ? state.user.accounts
        : [{ id: 'dev-acc', number: '000000000001', balance: 1000 }];
    const userId = state?.user?.idUsuarioWeb ?? null;

    const [step, setStep] = useState(1);
    const [toAccount, setToAccount] = useState("");
    const [bank, setBank] = useState("");
    const [toName, setToName] = useState("");
    const [fromAcc, setFromAcc] = useState(accounts[0].id);
    const fromAccount = accounts.find(a => a.id === fromAcc) || accounts[0];
    const [amount, setAmount] = useState("");
    const [error, setError] = useState("");

    const goToStep2 = () => {
        if (!toAccount || !bank || !toName)
            return setError("Todos los campos son obligatorios.");

        if (toAccount.length !== 12)
            return setError("El número de cuenta debe tener exactamente 12 dígitos.");

        // Bank name must match ARCBANK variants (case-insensitive).
        const b = bank.trim().toLowerCase();
        const allowed = ["arcbank", "arcback"];
        if (!allowed.includes(b)) {
            return setError('Solo se permiten transferencias a cuentas del banco ARCBANK (escribe "ARCBANK", "ArcBack" o "arcbank").');
        }

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
            const idUsuarioWeb = userId;
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
                    alert(err?.body || err?.message || 'Error en la transferencia')
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
                        <div style={styles.infoBar}>
                            Solo transferencias dentro del mismo banco ARCBANK. Escribe "ARCBANK", "ArcBack" o "arcbank".
                        </div>

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
                        <h2 style={styles.title}>Seleccionar cuenta y monto</h2>

                        <label>Desde cuenta</label>
                        <select style={styles.input} value={fromAcc} onChange={e => setFromAcc(e.target.value)}>
                            {accounts.map(a => (
                                <option key={a.id} value={a.id}>{a.number} - Saldo: ${a.balance}</option>
                            ))}
                        </select>

                        <label>Monto</label>
                        <input
                            style={styles.input}
                            value={amount}
                            onChange={e => setAmount(e.target.value.replace(/[^0-9.]/g, ''))}
                        />

                        {error && <p style={styles.error}>{error}</p>}

                        <div style={styles.buttonRow}>
                            <button style={styles.btnCancel} onClick={() => setStep(1)}>Volver</button>
                            <button style={styles.btn} onClick={goToStep3}>Confirmar</button>
                        </div>
                    </div>
                )}

                {step === 3 && (
                    <div style={styles.card}>
                        <h2 style={styles.title}>Resumen</h2>
                        <table style={styles.table}>
                            <tbody>
                                <tr><td>Desde</td><td>{fromAccount.number}</td></tr>
                                <tr><td>Para</td><td>{toName} — {toAccount}</td></tr>
                                <tr><td>Banco</td><td>{bank}</td></tr>
                                <tr><td>Monto</td><td>${Number(amount).toFixed(2)}</td></tr>
                            </tbody>
                        </table>
                        <div style={styles.buttonRow}>
                            <button style={styles.btnCancel} onClick={() => setStep(2)}>Volver</button>
                            <button style={styles.btn} onClick={confirmTransfer}>Realizar transferencia</button>
                        </div>
                    </div>
                )}

                {step === 4 && (
                    <div style={styles.card}>
                        <h2 style={styles.title}>Transferencia enviada</h2>
                        <p>Transferencia realizada con éxito.</p>
                        <div style={styles.buttonRow}>
                            <button style={styles.btn} onClick={() => navigate('/movimientos')}>Ver movimientos</button>
                            <button style={styles.btn} onClick={downloadReceipt}>📄 Descargar</button>
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
        buttonRow: {
            display: "flex",
            justifyContent: "space-between",
            marginTop: 20,
        },
    };

import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { FiArrowLeft, FiPhone, FiMail, FiHome } from "react-icons/fi";

export default function Perfil() {
    const { state, updateUser } = useAuth();

    const [step, setStep] = useState(1);
    const [field, setField] = useState(""); 

    const [value1, setValue1] = useState("");
    const [value2, setValue2] = useState("");
    const [error, setError] = useState("");

    const saveChanges = () => {
        setError("");

        if (field === "phone") {
            if (!/^\d{10}$/.test(value1))
                return setError("El número debe tener exactamente 10 dígitos.");
            if (value1 !== value2)
                return setError("Los números no coinciden.");
            updateUser({ phone: value1 });
        }

        if (field === "email") {
            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value1))
                return setError("Ingresa un correo válido.");
            updateUser({ email: value1 });
        }

        if (field === "address") {
            if (value1.trim().length < 4)
                return setError("La dirección es demasiado corta.");
            updateUser({ address: value1 });
        }

        setStep(1);
        setValue1("");
        setValue2("");
    };

    return (
        <div style={{ padding: 30 }}>

            {step === 1 && (
                <>
                    <div style={styles.headerCard}>
                        <div style={styles.userIcon}>👤</div>
                        <h2 style={styles.userName}>{state.user.name}</h2>
                    </div>

                    <h3 style={styles.subtitle}>Mantén actualizada tu información</h3>

                    <div style={styles.cardList}>

                        <div style={styles.cardItem}>
                            <FiPhone size={30} />
                            <div>
                                <div style={styles.itemTitle}>Número de celular</div>
                                <div style={styles.itemValue}>{state.user.phone}</div>
                            </div>
                            <button
                                style={styles.updateBtn}
                                onClick={() => {
                                    setField("phone");
                                    setStep(2);
                                }}
                            >
                                Actualizar
                            </button>
                        </div>

                        <div style={styles.cardItem}>
                            <FiMail size={30} />
                            <div>
                                <div style={styles.itemTitle}>Correo electrónico</div>
                                <div style={styles.itemValue}>{state.user.email}</div>
                            </div>
                            <button
                                style={styles.updateBtn}
                                onClick={() => {
                                    setField("email");
                                    setStep(3);
                                }}
                            >
                                Actualizar
                            </button>
                        </div>

                        <div style={styles.cardItem}>
                            <FiHome size={30} />
                            <div>
                                <div style={styles.itemTitle}>Domicilio</div>
                                <div style={styles.itemValue}>{state.user.address}</div>
                            </div>
                            <button
                                style={styles.updateBtn}
                                onClick={() => {
                                    setField("address");
                                    setStep(4);
                                }}
                            >
                                Actualizar
                            </button>
                        </div>
                    </div>
                </>
            )}

            {step === 2 && (
                <>
                    <div style={styles.headerBack}>
                        <FiArrowLeft size={25} onClick={() => setStep(1)} style={styles.backIcon} />
                        <h2 style={{ marginLeft: 10 }}>Número de celular</h2>
                    </div>

                    <label>Nuevo número de celular</label>
                    <input
                        style={styles.input}
                        maxLength={10}
                        value={value1}
                        onChange={(e) => setValue1(e.target.value.replace(/\D/g, ""))}
                    />

                    <label>Confirma nuevo número de celular</label>
                    <input
                        style={styles.input}
                        maxLength={10}
                        value={value2}
                        onChange={(e) => setValue2(e.target.value.replace(/\D/g, ""))}
                    />

                    {error && <p style={styles.error}>{error}</p>}

                    <button style={styles.btn} onClick={saveChanges}>Actualizar</button>
                </>
            )}

            {step === 3 && (
                <>
                    <div style={styles.headerBack}>
                        <FiArrowLeft size={25} onClick={() => setStep(1)} style={styles.backIcon} />
                        <h2 style={{ marginLeft: 10 }}>Correo electrónico</h2>
                    </div>

                    <label>Nuevo correo</label>
                    <input
                        style={styles.input}
                        value={value1}
                        onChange={(e) => setValue1(e.target.value)}
                    />

                    {error && <p style={styles.error}>{error}</p>}

                    <button style={styles.btn} onClick={saveChanges}>Actualizar</button>
                </>
            )}

            {step === 4 && (
                <>
                    <div style={styles.headerBack}>
                        <FiArrowLeft size={25} onClick={() => setStep(1)} style={styles.backIcon} />
                        <h2 style={{ marginLeft: 10 }}>Domicilio</h2>
                    </div>

                    <label>Nuevo domicilio</label>
                    <textarea
                        style={{ ...styles.input, height: 80 }}
                        value={value1}
                        onChange={(e) => setValue1(e.target.value)}
                    />

                    {error && <p style={styles.error}>{error}</p>}

                    <button style={styles.btn} onClick={saveChanges}>Actualizar</button>
                </>
            )}
        </div>
    );
}

const styles = {
    headerCard: {
        background: "#f4f4f4",
        padding: 20,
        textAlign: "center",
        borderRadius: 10,
        marginBottom: 25,
    },
    userIcon: {
        fontSize: 40,
        background: "#ffd54f",
        width: 60,
        height: 60,
        borderRadius: "50%",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        margin: "0 auto 10px",
    },
    userName: { margin: 0 },
    subtitle: { marginBottom: 10 },
    cardList: { display: "flex", flexDirection: "column", gap: 15 },

    cardItem: {
        display: "flex",
        alignItems: "center",
        gap: 15,
        border: "2px solid #cc8c00",
        padding: 15,
        borderRadius: 10,
    },
    itemTitle: { fontWeight: 700 },
    itemValue: { fontSize: 14, color: "#555" },
    updateBtn: {
        marginLeft: "auto",
        background: "transparent",
        color: "#0066cc",
        border: "none",
        cursor: "pointer",
        fontWeight: 600,
        textDecoration: "underline",
    },

    headerBack: {
        display: "flex",
        alignItems: "center",
        marginBottom: 20,
    },
    backIcon: {
        cursor: "pointer",
    },

    infoBox: {
        background: "#eee",
        padding: 15,
        borderRadius: 10,
        marginBottom: 20,
        fontSize: 14,
    },

    input: {
        width: "100%",
        padding: 12,
        marginBottom: 15,
        borderRadius: 10,
        border: "2px solid #cc8c00",
        fontSize: 15,
    },
    btn: {
        background: "#cc8c00",
        color: "white",
        padding: "10px 20px",
        borderRadius: 8,
        border: "none",
        cursor: "pointer",
        fontSize: 16,
    },
    error: { color: "red", marginBottom: 10 },
};

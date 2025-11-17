import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { transacciones, cuentas } from "../../services/api";
import logo from "../../assets/Logo.png";
import "./ValoresTransaccion.css";

export default function ValoresTransaccion() {
  const nav = useNavigate();

 
  const stored = localStorage.getItem("cajero");
  const cajero =
    stored ? JSON.parse(stored) : { nombreCompleto: "Cajero Demo" };
  const primerNombre = cajero?.nombreCompleto?.split(" ")[0] || "Cajero";


  const [numeroCuenta, setNumeroCuenta] = useState("");

  
  const [cliente, setCliente] = useState({
    nombres: "",
    apellidos: "",
    cedula: "",
    tipoCuenta: "",
  });

  const [monto, setMonto] = useState("");
  const [error, setError] = useState("");


  const buscarCuenta = async () => {
    setError("");

    
    if (!numeroCuenta || numeroCuenta.length !== 12) {
      setError("El número de cuenta debe tener exactamente 12 dígitos.");
      return;
    }

    try {
      const data = await cuentas.getCuenta(numeroCuenta);

     
      setCliente({
        nombres: data.nombres || "",
        apellidos: data.apellidos || "",
        cedula: data.identificacion || "",
        tipoCuenta: data.tipoCuenta || "",
      });
    } catch (e) {
      setCliente({
        nombres: "",
        apellidos: "",
        cedula: "",
        tipoCuenta: "",
      });
      setError("No existe una cuenta con ese número.");
    }
  };

  const cerrarSesion = () => {
    localStorage.removeItem("cajero");
    nav("/");
  };

  const irInicio = () => {
    nav("/seleccionar");
  };


  const continuarRetiro = async () => {
    setError("");

    if (!numeroCuenta || numeroCuenta.length !== 12) {
      setError("Debe ingresar un número de cuenta válido (12 dígitos).");
      return;
    }

    if (!monto || !/^\d+(,\d{1,2})?$/.test(monto)) {
      setError(
        "Monto inválido. Solo números y coma con máximo 2 decimales."
      );
      return;
    }

    const montoNumber = parseFloat(monto.replace(",", "."));

    try {
      
      const body = {
        numeroCuenta,
        monto: montoNumber,
        idCajero: cajero.idCajero,
        tipo: "RETIRO",
      };

      
      const response = await transacciones.retiro(body);

      
      const idTransaccion = response.idTransaccion || response.id || 1;

      nav(`/comprobante/${idTransaccion}`, {
        state: {
          tipo: "RETIRO",
          monto: montoNumber,
          costo: response.costo || 0,
          fecha:
            response.fecha ||
            new Date().toLocaleDateString("es-EC", {
              day: "2-digit",
              month: "2-digit",
              year: "numeric",
            }),
          sucursal: response.sucursal || "La Napo",
          cuenta: {
            nombre: `${cliente.nombres} ${cliente.apellidos}`.trim(),
            cedula: cliente.cedula,
            numero: numeroCuenta,
            banco: "ARCBANK",
          },
        },
      });
    } catch (err) {
      console.error(err);
      setError("Error al procesar el retiro.");
    }
  };

  return (
    <div className="retiro-page">

      <header className="retiro-header">
        <div className="rh-left">
          <img src={logo} alt="ARC BANK" className="rh-logo" />
          <div className="rh-cajero">
            <div className="rh-user-icon">
              <i className="fa-solid fa-user"></i>
            </div>
            <span className="rh-cajero-name">Cajero {primerNombre}</span>
          </div>
        </div>

        <div className="rh-center">RETIRO</div>

        <div className="rh-right">
          <button className="rh-link" onClick={irInicio}>
            <i className="fa-solid fa-house-chimney"></i> Inicio
          </button>
          <button className="rh-link" onClick={cerrarSesion}>
            <i className="fa-solid fa-right-from-bracket"></i> Cerrar sesión
          </button>
        </div>
      </header>


      <main className="retiro-main">

        <div className="retiro-search-row">
          <span className="cedula-label">Número de cuenta cliente</span>
          <div className="cedula-input-row">
            <input
              className="cedula-input"
              value={numeroCuenta}
              onChange={(e) => {
                const val = e.target.value;


                if (!/^\d*$/.test(val)) return;


                if (val.length > 12) {
                  setError(
                    "El número de cuenta no puede tener más de 12 dígitos."
                  );
                  return;
                }


                if (
                  error &&
                  (error.includes("12 dígitos") ||
                    error.includes("no puede tener más de 12"))
                ) {
                  setError("");
                }

                setNumeroCuenta(val);
              }}
              placeholder="000123456789"
            />
            <button className="btn-buscar" onClick={buscarCuenta}>
              Buscar
            </button>
          </div>
        </div>


        <section className="retiro-panel">

          <div className="retiro-left">
            <div className="retiro-field">
              <span className="label">Nombres</span>
              <input
                className="field-input"
                value={cliente.nombres}
                readOnly
              />
            </div>

            <div className="retiro-field">
              <span className="label">Apellidos</span>
              <input
                className="field-input"
                value={cliente.apellidos}
                readOnly
              />
            </div>

            <div className="retiro-field">
              <span className="label">Cédula</span>
              <input
                className="field-input"
                value={cliente.cedula}
                readOnly
              />
            </div>

            <div className="retiro-field">
              <span className="label">Tipo de Cuenta</span>
              <input
                className="field-input"
                value={cliente.tipoCuenta}
                readOnly
                placeholder="Ahorros / Corriente"
              />
            </div>
          </div>


          <div className="retiro-divider"></div>

          <div className="retiro-right">
            <div className="retiro-field">
              <span className="label">Monto a retirar</span>
              <input
                className="field-input monto-input"
                value={monto}
                onChange={(e) => {
                  const val = e.target.value;
                  if (val === "" || /^\d*(,\d{0,2})?$/.test(val)) {
                    setMonto(val);
                  }
                }}
                placeholder="$ 10"
              />
            </div>
          </div>
        </section>


        {error && <div className="retiro-error">{error}</div>}


        <div className="retiro-buttons">
          <button className="btn-amarillo" onClick={continuarRetiro}>
            Continuar
          </button>
          <button className="btn-amarillo btn-cancelar" onClick={irInicio}>
            Cancelar
          </button>
        </div>
      </main>
    </div>
  );
}

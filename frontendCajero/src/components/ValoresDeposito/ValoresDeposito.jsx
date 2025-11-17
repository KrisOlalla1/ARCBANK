import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { clientes, cuentas, transacciones } from "../../services/api"; 
import logo from "../../assets/Logo.png";
import "../ValoresTransaccion/ValoresTransaccion.css";

export default function ValoresDeposito() {
  const nav = useNavigate();


  const stored = localStorage.getItem("cajero");
  const cajero =
    stored ? JSON.parse(stored) : { nombreCompleto: "Cajero Demo" };
  const primerNombre = cajero?.nombreCompleto?.split(" ")[0] || "Cajero";

  const [cedulaDepositante, setCedulaDepositante] = useState("");
  const [numeroCuentaCliente, setNumeroCuentaCliente] = useState("");

  const [depositante, setDepositante] = useState({
    nombres: "",
    apellidos: "",
  });

  const [cliente, setCliente] = useState({
    nombres: "",
    apellidos: "",
    cedula: "",
    tipoCuenta: "",
  });

  const [monto, setMonto] = useState("");
  const [error, setError] = useState("");


  const buscarDepositante = async () => {
    setError("");

    if (!cedulaDepositante || cedulaDepositante.length !== 10) {
      setError("La cédula del depositante debe tener exactamente 10 dígitos.");
      return;
    }

    try {
      const data = await clientes.getByCedula(cedulaDepositante);
      
      setDepositante({
        nombres: data.nombres || "",
        apellidos: data.apellidos || "",
      });
    } catch (e) {
      setDepositante({
        nombres: "",
        apellidos: "",
      });
      setError("No existe un depositante con esa cédula.");
    }
  };


  const buscarCuentaPorNumero = async () => {
    setError("");

    if (!numeroCuentaCliente || numeroCuentaCliente.length !== 12) {
      setError("El número de cuenta debe tener exactamente 12 dígitos.");
      return;
    }

    try {
      const data = await cuentas.getCuenta(numeroCuentaCliente);

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

 const continuarDeposito = async () => {
  setError("");

  if (!numeroCuentaCliente || numeroCuentaCliente.length !== 12) {
    setError("Debe ingresar un número de cuenta válido (12 dígitos).");
    return;
  }

  if (!monto) {
    setError("Debe ingresar un monto válido.");
    return;
  }

  const montoNumber = parseFloat(monto.replace(",", "."));

  try {
    
    const body = {
      numeroCuenta: numeroCuentaCliente,
      monto: montoNumber,
      cedulaDepositante,
      idCajero: cajero.idCajero,
      tipo: "DEPOSITO"
    };

    const response = await transacciones.deposito(body);

    
    const idTransaccion = response.idTransaccion;

    
    nav(`/comprobante/${idTransaccion}`, {
      state: {
        tipo: "DEPOSITO",
        monto: montoNumber,
        costo: response.costo || 0,
        fecha: response.fecha || new Date().toLocaleDateString("es-EC"),
        sucursal: response.sucursal || "La Napo",

        depositante: {
          nombre: `${depositante.nombres} ${depositante.apellidos}`.trim(),
          identificacion: cedulaDepositante,
        },

        cuenta: {
          nombre: `${cliente.nombres} ${cliente.apellidos}`.trim(),
          cedula: cliente.cedula,
          numero: numeroCuentaCliente,
          banco: "ARCBANK",
        },
      },
    });
  } catch (err) {
    console.error(err);
    setError("Error al procesar el depósito.");
  }
};

  return (
    <div className="retiro-page deposito-page">
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

        <div className="rh-center">DEPOSITO</div>

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
        <section className="deposit-panel">
          <div className="deposit-top-row">

            <div className="deposit-col">
              <div className="cedula-label">Número de cédula depositante</div>

              <div className="cedula-input-row">
                <input
                  className="cedula-input"
                  value={cedulaDepositante}
                  onChange={(e) => {
                    const val = e.target.value;

                    if (!/^\d*$/.test(val)) return;

                    if (val.length > 10) {
                      setError(
                        "La cédula del depositante no puede tener más de 10 dígitos."
                      );
                      return;
                    }

                    if (error && error.includes("cédula")) setError("");

                    setCedulaDepositante(val);
                  }}
                  placeholder="1726589895"
                />
              </div>

              <div className="retiro-field">
                <span className="label">Nombres</span>
                <input 
                  className="field-input" 
                  value={depositante.nombres}
                  onChange={(e) => setDepositante({...depositante, nombres: e.target.value})}
                />
              </div>

              <div className="retiro-field">
                <span className="label">Apellidos</span>
                <input 
                  className="field-input" 
                  value={depositante.apellidos}
                  onChange={(e) => setDepositante({...depositante, apellidos: e.target.value})}
                />
              </div>
            </div>

            <div className="deposit-divider"></div>

            <div className="deposit-col">
              <div className="cedula-label">Número de cuenta cliente</div>

              <div className="cedula-input-row">
                <input
                  className="cedula-input"
                  value={numeroCuentaCliente}
                  onChange={(e) => {
                    const val = e.target.value;

                    if (!/^\d*$/.test(val)) return;

                    if (val.length > 12) {
                      setError("El número de cuenta no puede tener más de 12 dígitos.");
                      return;
                    }

                    if (error && error.includes("cuenta")) setError("");

                    setNumeroCuentaCliente(val);
                  }}
                  placeholder="2258102613"
                />

                <button className="btn-buscar" onClick={buscarCuentaPorNumero}>
                  Buscar
                </button>
              </div>

              <div className="retiro-field">
                <span className="label">Nombres</span>
                <input className="field-input" value={cliente.nombres} readOnly />
              </div>

              <div className="retiro-field">
                <span className="label">Apellidos</span>
                <input className="field-input" value={cliente.apellidos} readOnly />
              </div>

              <div className="retiro-field">
                <span className="label">Cédula</span>
                <input className="field-input" value={cliente.cedula} readOnly />
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
          </div>

          <div className="deposit-monto-row">
            <span className="label">Monto a depositar</span>
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

          <div className="deposit-buttons-row">
            <button className="btn-amarillo" onClick={continuarDeposito}>
                Continuar
            </button>

            <button className="btn-amarillo btn-cancelar" onClick={irInicio}>
                Cancelar
            </button>
          </div>
          {error && <div className="retiro-error">{error}</div>}
        </section>
      </main>
    </div>
  );
}

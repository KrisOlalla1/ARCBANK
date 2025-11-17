import { useLocation, useNavigate, useParams } from "react-router-dom";
import { jsPDF } from "jspdf";
import logo from "../../assets/logo.png";
import "./Comprobante.css";

export default function Comprobante() {
  const { id } = useParams(); 
  const nav = useNavigate();
  const stored = localStorage.getItem("cajero");
  const cajero =
    stored ? JSON.parse(stored) : { nombreCompleto: "Cajero Demo" };
  const primerNombre = cajero?.nombreCompleto?.split(" ")[0] || "Cajero";

  const location = useLocation();
  const state = location.state || {};

  const tipo = state.tipo || "RETIRO"; 

  const isDeposito = tipo === "DEPOSITO";
  const titulo = isDeposito ? "Deposito exitoso" : "Retiro exitoso";
  const tituloHeader = isDeposito ? "DEPOSITO" : "RETIRO";

  const monto = state.monto || 10.0;
  const costo = state.costo || 0.0;
  const fecha =
    state.fecha ||
    new Date().toLocaleDateString("es-EC", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
  const sucursal = state.sucursal || "La Napo";


  const depositante = state.depositante || {
    nombre: "",
    identificacion: "",
  };

  const cuenta = state.cuenta || {
    nombre: "",
    cedula: "",
    numero: "",
    banco: "ARCBANK",
  };

  const mascaraCuenta = (num) => {
    if (!num || num.length < 4) return num || "";
    const ultimos = num.slice(-4);
    return "******" + ultimos;
  };

  const cerrarSesion = () => {
    localStorage.removeItem("cajero");
    nav("/");
  };

  const irInicio = () => {
    nav("/seleccionar");
  };

  const handleImprimir = () => {
    const doc = new jsPDF();

    const labelOperacion = isDeposito ? "DEPÓSITO" : "RETIRO";

    doc.setFontSize(16);
    doc.text(`ARCBANK - Comprobante de ${labelOperacion}`, 20, 20);

    doc.setFontSize(12);
    doc.text(`Fecha: ${fecha}`, 20, 30);
    doc.text(`Sucursal: ${sucursal}`, 20, 36);
    doc.text(`Cajero: ${cajero.nombreCompleto || ""}`, 20, 42);

    doc.text(`Monto: $ ${monto.toFixed ? monto.toFixed(2) : monto}`, 20, 52);
    doc.text(`Costo de la transacción: $ ${costo.toFixed ? costo.toFixed(2) : costo}`, 20, 58);

    if (isDeposito) {
      doc.text("Datos del depositante:", 20, 70);
      doc.text(`Nombre: ${depositante.nombre || ""}`, 20, 76);
      doc.text(`Identificación: ${depositante.identificacion || ""}`, 20, 82);

      doc.text("Cuenta de destino:", 20, 94);
      doc.text(`Nombre: ${cuenta.nombre || ""}`, 20, 100);
      doc.text(`Cédula: ${cuenta.cedula || ""}`, 20, 106);
      doc.text(
        `Número de cuenta: ${cuenta.numero || ""}`,
        20,
        112
      );
      doc.text(`Banco: ${cuenta.banco || "ARCBANK"}`, 20, 118);
    } else {
      doc.text("Cuenta de origen:", 20, 70);
      doc.text(`Nombre: ${cuenta.nombre || ""}`, 20, 76);
      doc.text(`Cédula: ${cuenta.cedula || ""}`, 20, 82);
      doc.text(
        `Número de cuenta: ${cuenta.numero || ""}`,
        20,
        88
      );
    }

    if (id) {
      doc.text(`Id Transacción: ${id}`, 20, 132);
    }

    doc.save(
      `comprobante_${labelOperacion.toLowerCase()}_${new Date()
        .toISOString()
        .slice(0, 10)}.pdf`
    );
  };

  return (
    <div className="comp-page">

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

        <div className="rh-center">{tituloHeader}</div>

        <div className="rh-right">
          <button className="rh-link" onClick={irInicio}>
            <i className="fa-solid fa-house-chimney"></i> Inicio
          </button>
          <button className="rh-link" onClick={cerrarSesion}>
            <i className="fa-solid fa-right-from-bracket"></i> Cerrar sesión
          </button>
        </div>
      </header>

      <main className="comp-main">
        <section className="comp-card">
          <div className="comp-check">
            <div className="comp-check-circle">
              <i className="fa-solid fa-check"></i>
            </div>
            <h2 className="comp-title">{titulo}</h2>
          </div>

          <div className="comp-row">
            <div className="comp-col-left">
              <span className="comp-label-strong">
                {isDeposito ? "Monto a depositar" : "Monto a retirar"}
              </span>
              <span className="comp-label">Costo de la transacción</span>
              <span className="comp-label">Fecha de la transacción</span>
              <span className="comp-label">Sucursal</span>
            </div>

            <div className="comp-col-right">
              <span className="comp-amount">
                $ {monto.toFixed ? monto.toFixed(2) : monto}
              </span>
              <span className="comp-text">
                $ {costo.toFixed ? costo.toFixed(2) : costo}
              </span>
              <span className="comp-text">{fecha}</span>
              <span className="comp-text">{sucursal}</span>
            </div>
          </div>

          <hr className="comp-divider" />

          {isDeposito ? (
            <>
              <div className="comp-section-title">Depositante</div>
              <div className="comp-row-bottom">
                <div className="comp-col-left">
                  <span className="comp-label">Nombre:</span>
                  <span className="comp-label">No. identificación:</span>
                </div>
                <div className="comp-col-right">
                  <span className="comp-text">
                    {depositante.nombre || ""}
                  </span>
                  <span className="comp-text">
                    {depositante.identificacion || ""}
                  </span>
                </div>
              </div>

              <hr className="comp-divider" />

              <div className="comp-section-title">Cuenta de destino</div>
              <div className="comp-row-bottom">
                <div className="comp-col-left">
                  <span className="comp-label">Nombre:</span>
                  <span className="comp-label">No. de cuenta:</span>
                  <span className="comp-label">Banco:</span>
                </div>
                <div className="comp-col-right">
                  <span className="comp-text">{cuenta.nombre || ""}</span>
                  <span className="comp-text">
                    {mascaraCuenta(cuenta.numero)}
                  </span>
                  <span className="comp-text">
                    {cuenta.banco || "ARCBANK"}
                  </span>
                </div>
              </div>
            </>
          ) : (
            <>
              <div className="comp-section-title">Cuenta de origen</div>
              <div className="comp-row-bottom">
                <div className="comp-col-left">
                  <span className="comp-label">Nombre:</span>
                  <span className="comp-label">No. de cuenta:</span>
                </div>
                <div className="comp-col-right">
                  <span className="comp-text">{cuenta.nombre || ""}</span>
                  <span className="comp-text">
                    {mascaraCuenta(cuenta.numero)}
                  </span>
                </div>
              </div>
            </>
          )}

          <div className="comp-actions">
            <button className="btn-amarillo comp-print-btn" onClick={handleImprimir}>
              <i className="fa-solid fa-file-arrow-down"></i> Imprimir comprobante
            </button>
          </div>
        </section>
      </main>
    </div>
  );
}

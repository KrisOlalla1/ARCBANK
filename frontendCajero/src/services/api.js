const BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8082";

async function request(path, options = {}) {
  const url = `${BASE_URL}${path}`;
  console.log("🌐 Request a:", url);
  const res = await fetch(url, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  console.log("📡 Response status:", res.status, res.ok);
  if (!res.ok) {
    const txt = await res.text().catch(() => null);
    console.error("❌ Error response:", txt);
    throw new Error(txt || "Server error");
  }
  return res.status === 204 ? null : res.json();
}

export const clientes = {
  getByCedula: (cedula) => request(`/api/clientes/cedula/${cedula}`),
};

export const auth = {
  login: (usuario, clave) =>
    request("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ usuario, clave }),
    }),
};

export const cuentas = {
  getByCedula: (cedula) => request(`/api/clientes/cedula/${cedula}`),
  getByNumeroCuenta: (numero) => request(`/api/cuentas/numero/${numero}`),
  // Método unificado que intenta buscar por cédula o número de cuenta
  getCuenta: async (identificador) => {
    console.log("🔧 getCuenta llamado con:", identificador);
    // Si tiene 12 dígitos, es número de cuenta
    if (identificador && identificador.length === 12) {
      console.log("📋 Es número de cuenta, llamando a:", `/api/cuentas/numero/${identificador}`);
      return await request(`/api/cuentas/numero/${identificador}`);
    }
    // Si tiene 10 dígitos, es cédula
    if (identificador && identificador.length === 10) {
      console.log("📋 Es cédula, llamando a:", `/api/clientes/cedula/${identificador}`);
      return await request(`/api/clientes/cedula/${identificador}`);
    }
    throw new Error("Identificador inválido");
  }
};

export const transacciones = {
  retiro: (body) =>
    request("/api/cajero/retirar", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  deposito: (body) =>
    request("/api/cajero/depositar", {
      method: "POST",
      body: JSON.stringify(body),
    }),
};


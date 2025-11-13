// Variable global para el modal
let idResultadoParaActualizar = null;

// --- NAVEGACIÓN ---
document.addEventListener('DOMContentLoaded', () => {
    // Asigna el evento a los links del menú
    const navLinks = document.querySelectorAll('.sidebar nav li a');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const pageId = link.getAttribute('data-page');
            showPage(pageId);
        });
    });
    // Carga la página inicial
    showPage('page-dashboard');
});

async function showPage(pageId) {
    // --- LÓGICA DE LIMPIEZA DE FORMULARIOS ---
    if (document.getElementById('form-paciente')) document.getElementById('form-paciente').reset();
    if (document.getElementById('form-medico')) document.getElementById('form-medico').reset();
    if (document.getElementById('form-orden')) document.getElementById('form-orden').reset();
    if (document.getElementById('form-resultado')) document.getElementById('form-resultado').reset();

    // 1. Oculta todas las secciones
    document.querySelectorAll('.page-section').forEach(section => section.classList.remove('active'));
    // 2. Quita 'active' de todos los links
    document.querySelectorAll('.sidebar nav li a').forEach(link => link.classList.remove('active'));

    // 3. Muestra la sección correcta
    document.getElementById(pageId).classList.add('active');
    // 4. Marca el link correcto
    document.querySelector(`.sidebar a[data-page="${pageId}"]`).classList.add('active');

    // 5. Carga los datos necesarios para esa página
    if (pageId === 'page-dashboard') loadDashboardStats();
    if (pageId === 'page-pacientes') loadPacientes();
    if (pageId === 'page-medicos') loadMedicos();
    if (pageId === 'page-ordenes') loadDropdownsOrden();
    if (pageId === 'page-resultados') loadResultados();
    if (pageId === 'page-facturacion') loadDropdownsFacturacion();
}

// --- LÓGICA DEL DASHBOARD ---
async function loadDashboardStats() {
    try {
        const [pacRes, medRes, ordRes] = await Promise.all([
            fetch('/api/pacientes/listar'),
            fetch('/api/medicos/listar'),
            fetch('/api/laboratorio/orden/listar')
        ]);
        const pacientes = await pacRes.json();
        const medicos = await medRes.json();
        const ordenes = await ordRes.json();

        document.getElementById('stat-pacientes').innerText = pacientes.length;
        document.getElementById('stat-medicos').innerText = medicos.length;
        document.getElementById('stat-ordenes').innerText = ordenes.filter(o => o.entregado === false).length;
    } catch (e) { console.error("Error cargando estadísticas", e); }
}

// --- LÓGICA DE PACIENTES ---
async function registrarPaciente() {
    const datos = {
        dni: document.getElementById('pac-dni').value,
        nombres: document.getElementById('pac-nombres').value,
        apellidos: document.getElementById('pac-apellidos').value,
        fechaNacimiento: document.getElementById('pac-fechaNacimiento').value,
        email: document.getElementById('pac-email').value,
        telefono: document.getElementById('pac-telefono').value
    };
    const respuesta = await fetch('/api/pacientes/registrar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (respuesta.ok) {
        mostrarToast('Paciente registrado exitosamente.', 'success');
        document.getElementById('form-paciente').reset();
        loadPacientes();
    } else {
        const errorTexto = await respuesta.text();
        mostrarToast(errorTexto, 'error');
    }
}

async function loadPacientes() {
    const respuesta = await fetch('/api/pacientes/listar');
    const pacientes = await respuesta.json();
    const tablaBody = document.getElementById('tabla-pacientes-body');
    tablaBody.innerHTML = '';
    pacientes.forEach(pac => {
        tablaBody.innerHTML += `
            <tr>
                <td>${pac.idPaciente}</td><td>${pac.dni}</td><td>${pac.nombres}</td>
                <td>${pac.apellidos}</td><td>${pac.email}</td>
            </tr>`;
    });
}

// --- LÓGICA DE MÉDICOS ---
async function registrarMedico() {
    const datos = {
        cmp: document.getElementById('med-cmp').value,
        nombres: document.getElementById('med-nombres').value,
        apellidos: document.getElementById('med-apellidos').value,
        especialidad: document.getElementById('med-especialidad').value,
        telefono: document.getElementById('med-telefono').value
    };
    const respuesta = await fetch('/api/medicos/registrar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (respuesta.ok) {
        mostrarToast('Médico registrado exitosamente.', 'success');
        document.getElementById('form-medico').reset();
        loadMedicos();
    } else {
        const errorTexto = await respuesta.text();
        mostrarToast(errorTexto, 'error');
    }
}

async function loadMedicos() {
    const respuesta = await fetch('/api/medicos/listar');
    const medicos = await respuesta.json();
    const tablaBody = document.getElementById('tabla-medicos-body');
    tablaBody.innerHTML = '';
    medicos.forEach(med => {
        tablaBody.innerHTML += `
            <tr>
                <td>${med.idMedico}</td><td>${med.cmp}</td><td>${med.nombres}</td>
                <td>${med.apellidos}</td><td>${med.especialidad}</td>
            </tr>`;
    });
}

// --- LÓGICA DE ÓRDENES ---
async function loadDropdownsOrden() {
    const selectPac = document.getElementById('orden-paciente');
    const selectMed = document.getElementById('orden-medico');

    selectPac.innerHTML = '<option value="">Cargando...</option>';
    const resPac = await fetch('/api/pacientes/listar');
    const pacientes = await resPac.json();
    selectPac.innerHTML = '<option value="">-- Seleccione un Paciente --</option>';
    pacientes.forEach(pac => {
        selectPac.innerHTML += `<option value="${pac.idPaciente}">${pac.nombres} ${pac.apellidos} (DNI: ${pac.dni})</option>`;
    });

    selectMed.innerHTML = '<option value="">Cargando...</option>';
    const resMed = await fetch('/api/medicos/listar');
    const medicos = await resMed.json();
    selectMed.innerHTML = '<option value="">-- Seleccione un Médico --</option>';
    medicos.forEach(med => {
        selectMed.innerHTML += `<option value="${med.idMedico}">${med.nombres} ${med.apellidos} (CMP: ${med.cmp})</option>`;
    });
}

async function registrarOrden() {
    const idPaciente = document.getElementById('orden-paciente').value;
    const idMedico = document.getElementById('orden-medico').value;
    const datos = {
        tipoExamen: document.getElementById('orden-tipoExamen').value,
        observaciones: document.getElementById('orden-observaciones').value,
        paciente: { idPaciente: idPaciente },
        medico: { idMedico: idMedico }
    };
    const respuesta = await fetch('/api/laboratorio/orden/crear', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (respuesta.ok) {
        mostrarToast('Orden creada exitosamente.', 'success');
        document.getElementById('form-orden').reset();
    } else {
        const errorTexto = await respuesta.text();
        mostrarToast(errorTexto, 'error');
    }
}

// --- LÓGICA DE RESULTADOS ---
async function loadResultados() {
    const selectOrden = document.getElementById('res-orden');
    const tablaBody = document.getElementById('tabla-resultados-body');

    selectOrden.innerHTML = '<option value="">Cargando...</option>';
    const resOrdenes = await fetch('/api/laboratorio/orden/listar');
    const ordenes = await resOrdenes.json();
    selectOrden.innerHTML = '<option value="">-- Seleccione una Orden --</option>';
    ordenes.forEach(orden => {
        let textoOrden = `ID: ${orden.idOrden} - ${orden.tipoExamen}`;
        if (orden.paciente) textoOrden += ` (Pac: ${orden.paciente.nombres})`;
        selectOrden.innerHTML += `<option value="${orden.idOrden}">${textoOrden}</option>`;
    });

    tablaBody.innerHTML = '<tr><td colspan="6">Cargando...</td></tr>';
    const resResultados = await fetch('/api/laboratorio/resultado/listar');
    const resultados = await resResultados.json();
    tablaBody.innerHTML = '';

    resultados.forEach(res => {
        let estadoHtml = res.validado
            ? `<span style="color: #155724; font-weight: bold;">Validado</span>`
            : `<span style="color: #e67e22; font-weight: bold;">Pendiente</span>`;

        let accionesHtml = res.validado
            ? `<i>(Proceso finalizado)</i>`
            : `<button onclick="abrirModalActualizar(${res.idResultado}, '${res.valores}')">Actualizar</button>
               <button class="btn-secondary" onclick="validarResultado(${res.idResultado})">Validar</button>`;

        tablaBody.innerHTML += `
            <tr>
                <td>${res.idResultado}</td><td>${res.ordenLaboratorio.idOrden}</td><td>${res.descripcion}</td>
                <td>${res.valores}</td><td>${estadoHtml}</td><td>${accionesHtml}</td>
            </tr>`;
    });
}

async function registrarResultado() {
    const idOrden = document.getElementById('res-orden').value;
    const datos = {
        descripcion: document.getElementById('res-descripcion').value,
        valores: document.getElementById('res-valores').value,
        conclusiones: document.getElementById('res-conclusiones').value,
        ordenLaboratorio: { idOrden: idOrden }
    };
    const respuesta = await fetch('/api/laboratorio/resultado/registrar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (respuesta.ok) {
        mostrarToast('Resultado registrado exitosamente.', 'success');
        document.getElementById('form-resultado').reset();
        loadResultados();
    } else {
        const errorTexto = await respuesta.text();
        mostrarToast(errorTexto, 'error');
    }
}

async function validarResultado(idResultado) {
    if (!confirm('¿Está seguro de VALIDAR este resultado? Esta acción no se puede deshacer.')) return;
    const respuesta = await fetch(`/api/laboratorio/resultado/validar/${idResultado}`, { method: 'POST' });
    if (respuesta.ok) {
        mostrarToast("Resultado validado con éxito.", "success");
        loadResultados();
    } else { mostrarToast('Error al validar el resultado.', 'error'); }
}

// --- LÓGICA DE MODAL (HU-03) ---
function abrirModalActualizar(idResultado, valoresActuales) {
    idResultadoParaActualizar = idResultado;
    // Precarga el campo con el valor existente
    document.getElementById('modal-res-valores').value = valoresActuales;
    document.getElementById('modal-backdrop').classList.add('visible');
    document.getElementById('modal-res-valores').focus();
}

function cerrarModalActualizar() {
    document.getElementById('modal-backdrop').classList.remove('visible');
    document.getElementById('modal-res-valores').value = '';
    idResultadoParaActualizar = null;
}

async function guardarActualizacionResultado() {
    const nuevosValores = document.getElementById('modal-res-valores').value;
    if (!nuevosValores || nuevosValores.trim() === "") {
        mostrarToast("Debe ingresar un valor.", "error");
        return;
    }
    const respuesta = await fetch(`/api/laboratorio/resultado/actualizar/${idResultadoParaActualizar}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'text/plain' },
        body: nuevosValores
    });
    if (respuesta.ok) {
        mostrarToast("Resultado actualizado exitosamente.", "success");
        cerrarModalActualizar();
        loadResultados();
    } else {
        const errorTexto = await respuesta.text();
        mostrarToast(errorTexto, "error");
    }
}

// --- LÓGICA DE FACTURACIÓN (HU-05) ---
async function loadDropdownsFacturacion() {
    const selectPac = document.getElementById('fact-paciente');
    selectPac.innerHTML = '<option value="">Cargando...</option>';
    const resPac = await fetch('/api/pacientes/listar');
    const pacientes = await resPac.json();
    selectPac.innerHTML = '<option value="">-- Seleccione un Paciente --</option>';
    pacientes.forEach(pac => {
        selectPac.innerHTML += `<option value="${pac.idPaciente}">${pac.nombres} ${pac.apellidos} (DNI: ${pac.dni})</option>`;
    });
}

function agregarDetalleFactura() {
    const container = document.getElementById('factura-detalles-container');
    const nuevoItemHtml = `
        <div class="form-grid detalle-item">
            <div class="input-group"><label>Descripción del Servicio:</label><input type="text" class="fact-detalle-desc"></div>
            <div class="input-group"><label>Cantidad:</label><input type="number" class="fact-detalle-cant" value="1"></div>
            <div class="input-group"><label>Precio Unitario (S/):</label><input type="number" class="fact-detalle-precio"></div>
        </div>`;
    container.insertAdjacentHTML('beforeend', nuevoItemHtml);
}

async function registrarFactura() {
    let facturaRequest = { factura: {}, detalles: [] };
    const idPaciente = document.getElementById('fact-paciente').value;
    facturaRequest.factura = {
        metodoPago: document.getElementById('fact-metodo-pago').value,
        paciente: { idPaciente: idPaciente }
    };
    const detalleItems = document.querySelectorAll('.detalle-item');
    for (const item of detalleItems) {
        const descripcion = item.querySelector('.fact-detalle-desc').value;
        const cantidad = item.querySelector('.fact-detalle-cant').value;
        const precio = item.querySelector('.fact-detalle-precio').value;
        if (!descripcion || !cantidad || !precio) {
            mostrarToast("Error: Todos los campos de detalle son obligatorios.", 'error');
            return;
        }
        facturaRequest.detalles.push({
            descripcionServicio: descripcion,
            cantidad: parseInt(cantidad),
            precioUnitario: parseFloat(precio)
        });
    }
    const respuesta = await fetch('/api/facturacion/crear', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(facturaRequest)
    });
    if (respuesta.ok) {
        mostrarToast('Factura creada exitosamente.', 'success');
        document.getElementById('factura-detalles-container').innerHTML = '';
        agregarDetalleFactura();
        document.getElementById('fact-paciente').value = '';
    } else {
        const errorTexto = await respuesta.text();
        mostrarToast(errorTexto, 'error');
    }
}

// --- ¡NUEVO! FUNCIÓN UTILITARIA (TOAST) ---
function mostrarToast(mensaje, tipo = 'success') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast ${tipo}`;
    const mensajeLimpio = mensaje.replace("ERROR (HU03):", "").replace("Error:", "");
    toast.innerHTML = mensajeLimpio;
    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('fade-out');
        setTimeout(() => {
            toast.remove();
        }, 500);
    }, 3000);
}

// --- LÓGICA DE AUTENTICACIÓN (LOGIN/REGISTRO) ---
// (Estas funciones son llamadas solo desde login.html)

async function login() {
    const datos = {
        nombreUsuario: document.getElementById('login-user').value,
        contrasena: document.getElementById('login-pass').value
    };

    const respuesta = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    if (respuesta.ok) {
        const usuario = await respuesta.json();
        // ¡Guardamos al usuario en la sesión del navegador!
        sessionStorage.setItem('usuarioLogueado', JSON.stringify(usuario));

        mostrarToast("¡Bienvenido, " + usuario.nombreUsuario + "!", "success");

        // Lo redirigimos al dashboard principal
        setTimeout(() => {
            window.location.href = '/index.html';
        }, 1000); // Espera 1 segundo

    } else {
        const errorTexto = await respuesta.text();
        mostrarToast(errorTexto, 'error');
    }
}

async function registrarNuevoUsuario() {

}
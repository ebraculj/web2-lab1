// Inicijalizacija Auth0 klijenta
/*let auth0Client = null;

async function initAuth0() {
    auth0Client = await createAuth0Client({
        domain: "dev-8mv31s8w7thhyqjj.eu.auth0.com",
        client_id: "hQhsuOb70FKsqXe4t89BNRkCPZ1OouxC",
        redirect_uri: window.location.href // Nakon prijave vraća se na istu stranicu
    });
}

async function checkLoginAndFetchTicket() {
    await initAuth0();

    const isAuthenticated = await auth0Client.isAuthenticated();

    if (!isAuthenticated) {
        // Ako korisnik nije prijavljen, preusmjerite ga na Auth0 login stranicu
        await auth0Client.loginWithRedirect();
    } else {
        // Ako je prijavljen, dohvatite podatke o ulaznici
        fetchTicketData();
    }
}

async function fetchTicketData() {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('id');

    if (!uuid) {
        document.getElementById("ticket-info").innerText = "Nema UUID-a u URL-u.";
        return;
    }

    try {
        // Dohvaćanje tokena za autentifikaciju
        const token = await auth0Client.getTokenSilently();

        const response = await fetch(`http://localhost:8080/api/ticket/ticket-data/${uuid}`, {
            headers: {
                Authorization: `Bearer ${token}` // Autorizacija zahtjeva s tokenom
            }
        });

        if (!response.ok) {
            throw new Error(`Greška: ${response.status} - ${response.statusText}`);
        }

        const ticket = await response.json();
        displayTicketData(ticket);
    } catch (error) {
        document.getElementById("ticket-info").innerText = error.message;
    }
}

function displayTicketData(ticket) {
    const ticketInfoDiv = document.getElementById("ticket-info");
    ticketInfoDiv.innerHTML = `
        <p><strong>Ime:</strong> ${ticket.firstName}</p>
        <p><strong>Prezime:</strong> ${ticket.lastName}</p>
        <p><strong>OIB:</strong> ${ticket.vatin}</p>
        <p><strong>Datum i vrijeme:</strong> ${new Date(ticket.dateTime).toLocaleString()}</p>
    `;
}*/
async function fetchTicketCount() {
    try {
        //const token = await auth0Client.getTokenSilently();
        const response = await fetch('http://localhost:8080/api/ticket/count');

        if (!response.ok) {
            throw new Error(`Greška: ${response.status} - ${response.statusText}`);
        }

        const count = await response.json();
        console.log(count);
        const ticketCountEl = document.getElementById("ticket-count");

        if (ticketCountEl) {
            ticketCountEl.innerText = count;
        } else {
            console.error("Element 'ticket-count' nije pronađen.");
        }
    } catch (error) {
        console.error('Greška pri dohvaćanju broja ulaznica:', error);
        const errorMessageElement = document.getElementById("error-message");
        if (errorMessageElement) {
            errorMessageElement.innerText = error.message;
        }
    }
}
window.onload = function () {
    //checkLoginAndFetchTicket(); // Provjera prijave i dohvaćanje podataka
    fetchTicketCount(); // Dohvaćanje broja ulaznica
};

let request = new XMLHttpRequest();
request.open("GET", "movies.json");

request.responseType = "json";
request.send();

// после загрузки файла JSON
request.onload = function() {
    let movie = request.response;
    fillTable(movie);
}

// заполнение таблицы из JSON файла
function fillTable(movie) {
    let tbody = document.querySelector("tbody");

    movie.forEach(movie => {
        let newRow = document.createElement("tr");

        newRow.innerHTML = `<td>${movie["name"]}</td><td>${movie["director"]}</td><td>${movie["year"]}</td><td>${movie["fees"]}</td><td>${movie["genre"]}</td><td>${movie["budget"]}</td>`;
        tbody.appendChild(newRow);
    });
}
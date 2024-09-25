const evalButton = document.getElementById("evalButton")
const progress = document.getElementById("evalProgress")
const equalIcon = document.getElementById("equalIcon")
const inputField = document.getElementById("inputField")
const history = document.getElementsByClassName("clickableHistory")
const table = document.getElementById("historyTable");
const user = document.getElementById("user").textContent
const destroy = document.getElementById("destroy")

destroy.onclick = () => {
    fetch("/destroy", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: {}
    }).then(
        async response => {
            if (response.redirected) {
                window.location.assign(response.url)
            }
        }
    )
}
evalButton.onclick = () => {
    equalIcon.style.display = "none"
    progress.style.display = "block"
    const data = {
        "expression": inputField.value
    }

    fetch("/calculator/evaluate", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    }).then(async json => {
            let text = await json.text()
            let id = text.substring(0, text.indexOf("#"))
            let result = text.substring(text.indexOf("#") + 1)
            inputField.value = result
            equalIcon.style.display = "block"
            progress.style.display = "none"

            var row = table.insertRow(1)
            row.innerHTML =
                `<td class="min left-align">` + id + `</td>
                <td class="max left-align">` + user + `</td>
                <td class="max center-align"><a class="clickableHistory">` + data.expression + `</a></td>
                <td class="min right-align"><a class="clickableHistory">` + inputField.value + `</a></td>`
        }
    )
}

for (let i = 0; i < history.length; i++) {
    history[i].addEventListener("click", () => {
        inputField.value += history[i].textContent
    })
}
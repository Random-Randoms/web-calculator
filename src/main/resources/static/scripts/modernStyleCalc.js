const evalButton = document.getElementById("evalButton")
const progress = document.getElementById("evalProgress")
const equalIcon = document.getElementById("equalIcon")
const inputField = document.getElementById("inputField")
const history = document.getElementsByClassName("clickableHistory")

evalButton.onclick = () => {
    equalIcon.hidden = true
    progress.hidden = false
    const data = {
        "expression": inputField.value
    }

    fetch("/calculator/evaluate", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    }).then(async json => {
            inputField.value = await json.text()
            equalIcon.hidden = false
            progress.hidden = true

        }
    )
}

for (let i = 0; i < history.length; i++) {
    history[i].addEventListener("click", () => {
        inputField.value+=history[i].textContent
        })
}
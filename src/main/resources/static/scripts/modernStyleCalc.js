const evalButton = document.getElementById("evalButton")
const progress = document.getElementById("evalProgress")
const equalIcon = document.getElementById("equalIcon")
const inputField = document.getElementById("inputField")


evalButton.onclick = () => {
    equalIcon.hidden = true
    progress.hidden = false
    const data = {
        "expression": inputField.value
    }

    alert(inputField.value)

    fetch("/calculator/evaluate", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    }).then(response => response.json())
        .then(json => {
            alert("success")
            inputField.value = json.evaluated
            equalIcon.hidden = false
            progress.hidden = true
        }
    )
}
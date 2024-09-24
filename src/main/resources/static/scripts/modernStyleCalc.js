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
    }).then(async json => {
            alert("success")
            inputField.value = await json.text()// json.body.getReader().read().then(({ done, value }) => {
            //     while (!done){
            //     }
            //    return value
            // })
            equalIcon.hidden = false
            progress.hidden = true
        }
    )
}
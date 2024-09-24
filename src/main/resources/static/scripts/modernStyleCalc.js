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
            if(response.redirected){
                window.location.assign(response.url)
            }
        }
    )
}
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
            let result = await json.text()
            inputField.value = result
            equalIcon.hidden = false
            progress.hidden = true

            var row = table.insertRow(1)
            row.innerHTML =`
                <td class="max left-align">`+user+`</td>
                <td class="max center-align"><a class="clickableHistory">`+data.expression+`</a></td>
                <td class="min right-align"><a class="clickableHistory">`+result+`</a></td>`
        }
    )
}

for (let i = 0; i < history.length; i++) {
    history[i].addEventListener("click", () => {
        inputField.value += history[i].textContent
    })
}
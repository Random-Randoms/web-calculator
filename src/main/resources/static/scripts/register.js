const register = document.getElementById("register")
const password = document.getElementById("password")
const password2 = document.getElementById("password2")
const span = document.getElementById("span")
const progress = document.getElementById("progress")
const form = document.querySelector("form")

form.addEventListener("submit", (e) => {
    // document.getElementById("signIn").disable()
    e.preventDefault();
    if (password.value!==password2.value) {
        password2.parentElement.classList.add("invalid")
        password2.value = ""
        document.getElementById("similarPasswordsErr").hidden=false
        return
    }
    const data = {
        "login": login.value,
        "password": CryptoJS.SHA256(password.value).toString(),
    }
    span.style.display = "none"
    progress.style.display = ""
    fetch("/register", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    }).then(async response => {
        if (response.redirected) {
            window.location.assign(response.url)
        } else {
            alert(await json.text())
            span.style.display = ""
            progress.style.display = "none"
            password.parentElement.classList.add("invalid")
            login.parentElement.classList.add("invalid")
            password.parentElement.insertAdjacentHTML("beforeend", "<span class='error'>Invalid login or password</span>")
        }
    })
})

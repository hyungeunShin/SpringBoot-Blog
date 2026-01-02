const token = searchParam('token')

if(token) {
    console.log(token);
    localStorage.setItem("access_token", token)
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}
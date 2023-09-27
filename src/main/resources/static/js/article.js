const delBtn = document.querySelector("#delete-btn")
const modifyBtn = document.querySelector("#modify-btn")
const createBtn = document.querySelector("#create-btn")

if(delBtn) {
    delBtn.addEventListener("click", event => {
        let id = document.querySelector("#article-id").value;
        fetch(`/api/articles/${id}`, {
            method: 'DELETE'
        })
        .then(() => {
            alert("삭제가 완료되었습니다.");
            location.replace("/articles");
        });
    });
}

if(modifyBtn) {
    modifyBtn.addEventListener("click", event => {
        let params = new URLSearchParams(location.search);
        let id = params.get("id");

        fetch(`/api/articles/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                title: document.querySelector("#title").value,
                content: document.querySelector("#content").value
            })
        })
        .then(() => {
            alert("수정이 완료되었습니다.");
            location.replace(`/articles/${id}`);
        });
    });
}

if(createBtn) {
    createBtn.addEventListener("click", event => {
        fetch(`/api/articles`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                title: document.querySelector("#title").value,
                content: document.querySelector("#content").value
            })
        })
        .then(() => {
            alert("등록이 완료되었습니다.");
            location.replace(`/articles`);
        });
    });
}
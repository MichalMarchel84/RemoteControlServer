const lis = [...document.querySelector("#scripts").children];
lis.forEach(li => {
    li.querySelector("#edit").addEventListener("click", () => toggleVisible(li));
    li.querySelector("#delete").addEventListener("click", () => deleteScript(li.dataset.id));
});

function deleteScript(id){
    if(confirm("Sure delete?")){
        fetch("/user/delete-script?scriptId=" + id,
            {}).then(resp => {
            if(resp.ok){
                document.querySelector("#id" + id).remove();
            }
        })
    }
}

function toggleVisible(li){
    const form = li.querySelector("form");
    const txt = li.querySelector("textarea");
    if(form.style.display === "none") {
        form.style.display = "flex";
        txt.style.display = "flex";
    }
    else {
        form.style.display = "none";
        txt.style.display = "none";
    }
}
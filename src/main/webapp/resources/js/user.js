setup();

function setup(){
    const lis = [...document.querySelector("#robots").children];
    lis.forEach(li => {
        li.querySelector("#delete").addEventListener("click", () => deleteDevice(li.id.replace("id", "")));
        setStatus(li, li.dataset.status);
    });
}

function setStatus(listItem, status){

    const btn = listItem.querySelector("button");
    const lamp = btn.querySelector("canvas");
    switch (status){
        case "0":
            btn.disabled = true;
            lamp.className = "lamp-red";
            break;
        case "1":
            btn.disabled = false;
            lamp.className = "lamp-green";
            break;
        case "2":
            btn.disabled = true;
            lamp.className = "lamp-blue";
            break;
    }
}

function deleteDevice(id){
    if(confirm("Sure delete?")){
        fetch("/user/delete?robotId=" + id,
            {}).then(resp => {
                if(resp.ok){
                    document.querySelector("#id" + id).remove();
                }
        })
    }
}
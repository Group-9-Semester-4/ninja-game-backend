function toggle(source) {
    checkboxes = document.getElementsByName('cards');
    for(var i=0, n=checkboxes.length;i<n;i++) {
        checkboxes[i].checked = source.checked;
    }
}


// todo refactor to one js file
function confirmAction() {
    var ask = window.confirm("Are you sure you want to leave? Your changes will not be saved.");
    if (ask) {
        window.location.href = "/";
    }
}
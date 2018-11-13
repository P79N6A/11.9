/**
 * Created by root on 16/9/18.
 */
function lister() {
    if (document.body.offsetHeight > document.documentElement.clientHeight) {
        var footer = document.querySelector("#mod-footer");
        if (footer != null) {
            footer.style.position = "static";
            footer.style.marginTop = "20px";
        }
    }
}
window.addEventListener("load", lister, false);
window.addEventListener("scroll", lister, false);
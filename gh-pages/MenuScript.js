function Ripple (event, e)
{
    let rippleeffect = document.createElement("span");
    rippleeffect.classList.add("ripple");
    e.appendChild(rippleeffect);
    let x = event.clientX - e.offsetLeft;
    let y = event.clientY - e.offsetTop;
    rippleeffect.style.left = `${x}px`;
    rippleeffect.style.top = `${y}px`;
    console.log(x, y);
    setTimeout(() => 
    {
        rippleeffect.remove();
    }, 300);
}
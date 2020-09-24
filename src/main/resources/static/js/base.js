/**
 * 
 */
function hexToRgbNew(hex) {
	var arrBuff = new ArrayBuffer(4);
	var vw = new DataView(arrBuff);
	vw.setUint32(0,parseInt(hex, 16),false);
	var arrByte = new Uint8Array(arrBuff);
	
	return arrByte[1] + "," + arrByte[2] + "," + arrByte[3];
}
const rgbToHex = (r, g, b) => '#' + [r, g, b].map(x => {
	  const hex = x.toString(16)
	  return hex.length === 1 ? '0' + hex : hex
	}).join('');

function rgbTorgba(rgb, opacity){
	return 'rgba('+rgb+',' + opacity + ')'
}
function hexTorgba(h, opacity){
	let r = 0, g = 0, b = 0;
	
	// 3 digits
	if (h.length == 4) {
		r = "0x" + h[1] + h[1];
		g = "0x" + h[2] + h[2];
		b = "0x" + h[3] + h[3];
	
	// 6 digits
	} else if (h.length == 7) {
		r = "0x" + h[1] + h[2];
		g = "0x" + h[3] + h[4];
		b = "0x" + h[5] + h[6];
	}
	r = Number(r);
	g = Number(g);
	b = Number(b);
    return 'rgba(' + r + ',' + g + ',' + b + ',' + opacity + ')';
}
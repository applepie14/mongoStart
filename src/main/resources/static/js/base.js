/**
 * 
 */

(function ($) {
	$('#memoContent').richText();
	
	$(".file-form").addClass("d-none");
	$(".custom-file-input").on("change", function() {
		var fileName = $(this).val().split("\\").pop();
		$(this).siblings(".custom-file-label").addClass("selected").html(fileName);
	});
	
	$(".file-form").addClass("d-none");
	$('#memoType').change(function(){
		if(!($(this).val() == 'photo')){
			$(".file-form").addClass("d-none");
		}else{
			$(".file-form").removeClass("d-none");
		}
	})
	
	$('#memoEnter').click(function(){
		let memoType = $('#memoType').val();
		let memoTitle = $('#memoTitle').val();
		let memoContent = $('.richText-editor').html();

		params = new FormData();
		params.set('memoType', memoType);
		params.set('memoPhoto', $('#memoPhoto')[0].files[0]);
		params.set('memoTitle', memoTitle);
		params.set('memoContent', memoContent);
		
		let result = false;
		if($(this).data('isNew') && memoType == 'photo'){
			result = !memoType || !memoTitle || !memoContent || !$('#memoPhoto').val();
		}else{
			result = !memoType || !memoTitle || !memoContent
		}
		
		if(result){
			return false;
		} 
		$.ajax({
			type: "POST",
			url: '/memo/insert',
			data : params,
			processData : false,
			contentType : false,
			success: function (data) {
				$('#MemoModal').modal('hide');
				
				setMemo();
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.status);
				console.debug(jqXHR.responseText);
				console.log(errorThrown);
			}
		})
	})
	
	$('#memoUpdate').click(function(){
		let memoType = $('#memoType').val();
		let memoTitle = $('#memoTitle').val();
		let memoContent = $('.richText-editor').html();
		let memoObjectId = $('#MemoModal').data('memoObjectId');

		params = new FormData();
		params.set('memoObjectId', memoObjectId == undefined ? '' : memoObjectId);
		params.set('memoType', memoType);
		params.set('memoPhoto', $('#memoPhoto')[0].files[0]);
		params.set('memoTitle', memoTitle);
		params.set('memoContent', memoContent);
		
		let result = false;
		if($(this).data('isNew') && memoType == 'photo'){
			result = !memoType || !memoTitle || !memoContent || !$('#memoPhoto').val();
		}else{
			result = !memoType || !memoTitle || !memoContent
		}
		
		if(result){
			return false;
		} 
		$.ajax({
			type: "POST",
			url: '/memo/update',
			data : params,
			processData : false,
			contentType : false,
			success: function (data) {
				$('#MemoModal').modal('hide');
				
				setMemo();
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.status);
				console.debug(jqXHR.responseText);
				console.log(errorThrown);
			}
		})
	})
	$('#memoDelete').click(function(){
		let params = $('#MemoModal').data('memoObjectId');
		$.ajax({
			type: "delete",
			url: '/memo/delete/'+params,
			data: JSON.stringify(params),
			dataType: 'json',
			success: function (data) {
				$('#MemoModal').modal('hide');
				
				setMemo();
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.status);
				console.debug(jqXHR.responseText);
				console.log(errorThrown);
			}
		})
	});
	
	$('#MemoModal').data('isNew', true);
	$('#MemoModal').on('show.bs.modal', function(){
		if(!$(this).data('isNew')){
			$('#MemoModal .modal-header .modal-title').html('EDIT MEMO');
		}
	})
	$('#MemoModal').on('hide.bs.modal', function(){
		$('#memoEnter').removeClass('d-none');
		$('#memoUpdate').addClass('d-none');
		$('#memoDelete').addClass('d-none');
		
		$('#memoType').val('');
		$('#memoTitle').val('');
		$('#memoContent').val('');
		$('.richText-editor').html('');
		$('#memoPhoto').val('');
		$('#MemoModal .modal-header .modal-title').html('NEW MEMO');
		$(".custom-file-label").html('MEMO PHOTO');
		$(this).data('isNew', true);
		$(this).data('memoObjectId', '');
	})
}(jQuery));

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


$('#MemoModalOpenBtn').click(function(){
	$('#MemoModal').modal('show');
})


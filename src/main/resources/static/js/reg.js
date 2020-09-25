/**
 * 
 */
(function ($) {
	$('input').val('');
	
	$('input[placeholder]').placeholderLabel();
	$('input[name="reg_id"]').keyup(function(){
		if($(this).val() != ''){
			let params = { 'reg_id' : $(this).val() }
			$.ajax({
				type: "POST",
				url: '/member/idCheck/' +  $(this).val(),
				data: params,
				dataType:"html",
				success: function (data) {
					let idLabel  = $('.label-inputLoginId');
					if(data > 0){
						idLabel.data('confirm', false);
						idLabel.text('사용 중인 아이디입니다.');
						idLabel.addClass('text-danger');
						idLabel.removeClass('text-success');
					}
					else{
						idLabel.data('confirm', true);
						idLabel.text('사용 가능한 아이디입니다.');
						idLabel.removeClass('text-danger');
						idLabel.addClass('text-success');
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
					console.log(jqXHR.status);
					console.debug(jqXHR.responseText);
					console.log(errorThrown);
				}
			})
		}
		
	});
	$('#regForm').keydown(function (e) {
		if (e.keyCode === 13) {
			e.preventDefault();
			reg();
		}
	});
	$('#regBtn').click(function(){ reg(); })
	
	function reg(){
		let result = $('.label-inputLoginId').data('confirm');
		if(result){
			$('#regForm').submit();
		}else{
			alert('아이디를 변경하세요');
		}
	};
	
}(jQuery));
// End ready
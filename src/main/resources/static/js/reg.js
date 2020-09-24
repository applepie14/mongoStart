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
						idLabel.text('사용 중인 아이디입니다.');
						idLabel.addClass('text-danger');
						idLabel.removeClass('text-success');
					}
					else{
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
			$('#regForm').submit();
		}
	});
	$('#regBtn').click(function(){ reg(); })
	
	function reg(){
		let params = { 'reg_id' : $('#reg_id').val() }
		params.reg_pwd = $('#reg_pwd').val();
		params.reg_email = $('#reg_email').val();
		params.reg_name = $('#reg_name').val();
		
		$.ajax({
			type: "POST",
			url: '/member/register',
			data: params,
			dataType:"html",
			success: function (data) {
				
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.status);
				console.debug(jqXHR.responseText);
				console.log(errorThrown);
			}
		})
		
	};
	
}(jQuery));
// End ready
/**
 * 
 */
(function ($) {
	$('#commentBox .card').remove();
	getGuestbookList();

	var editor = new Simditor({
		  textarea: $('#GuestbookModal #send_content')
		  //optional options
		});
	
	
	// GuestBook 
	// - 사용자 목록 불러오기
	let getUserIdList = function(){
		$.ajax({
			type: "POST",
			url: '/guestbook/user-list',
			success: function (data) {
				$.each(data.userList, function(idx, i){
					$('#userList').append(`<option value="${i}">${i}</option>`);
					if(idx != 0){
						$('#modalUserList').append(`<option value="${i}">${i}</option>`);
					}else{
						$('#userList').val(i);
					}
				})
				$('#userList').change(function(){
					$('#commentBox .card').remove();
					getGuestbookList($(this).val());
				});
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.status);
				console.debug(jqXHR.responseText);
				console.log(errorThrown);
			}
		})
	}();
	
	// 방명록 작성
	$('#GuestbookModal #guestbookEnter').click(function(){
		let params = { 
				'owner' :  $('#GuestbookModal #modalUserList').val() ,
				'send_content' : $('#GuestbookModal #send_content').val()
			};
		$.ajax({
			type: "POST",
			url: '/guestbook/addGuestbook',
			data: params,
			success: function (data) {
//				console.log(data);

				$('#GuestbookModal').modal('hide');
				$('#GuestbookModal #modalUserList').val('');
				$('#GuestbookModal #send_content').val('');
				$('#GuestbookModal .simditor-body').html('');
				
				$('#commentBox .card').remove();
				$('#userList').val(params.owner);
				getGuestbookList(params.owner);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.status);
				console.log(jqXHR.responseText);
				console.log(errorThrown);
			}
		})
		
	})
}(jQuery));

// GuestBook 
function getGuestbookList(userId){
//	$('#commentBox').html('');
	$.ajax({
		type: "POST",
		url: '/guestbook/guestbook-list',
		data: { 'userId' : userId },
		success: function (data) {
			$.each(data.guestbookResult, function(idx, i){
				let result = i;
				let str_comments = '';
				$.each(result.comments, function(jj, j){
					str_comments += 
						`<div class="comment pt-3 px-3">
							<h6 class="comment-auth"><i class="fa fa-comments-o"></i> ${j.send_user}<span class="font-11 ml-3 font-weight-light">${j.send_dttm}</span></h6>
							<p class="comment-text card-text">${j.send_content}</p>
						</div>`;
				})
				let deleteContent = '';
				if(userSession == result.send_user){
					deleteContent =`<div class="editDiv float-right" data-objid="${result.objectId}">
						<i class="fa fa-trash-o"></i>
					</div>`;
				}
				
				let str = `<div class="card mt-3 ">
								<div class="card-body">
									<h5 class="card-title">
										${result.send_user}<span class="font-12 ml-3 font-weight-light">${result.send_dttm}</span>
										${deleteContent}
									</h5>
									<p class="card-text">${result.send_content}</p>
									<div class="comments">
										${str_comments}
									</div>
									<div class="live-comment-div mt-3">
										<textarea class="form-control"></textarea>
										<button class="btn btn-warning commentBtn" data-objid="${result.objectId}">COMMENT</button>
									</div>
				    			</div>
							</div>`;
				$('#commentBox').append(str);
			})

			$('.commentBtn').click(function(){
				let params = { 
						'objid' : $(this).data().objid ,
						'send_content' : $(this).prev().val()
					};
				let $this = $(this);
				if(params.send_content != ''){
					$.ajax({
						type: "POST",
						url: '/guestbook/addGuestbookComments',
						data: params,
						success: function (data) {
	//						console.log(data);
							let result = data.addGuestbookCommentResult;
							let str_comments = 
								`<div class="comment pt-3 px-3">
									<h6 class="comment-auth"><i class="fa fa-comments-o"></i> ${result.send_user}<span class="font-11 ml-3 font-weight-light">${result.send_dttm}</span></h6>
									<p class="comment-text card-text">${result.send_content}</p>
								</div>`;
							
							$($this).parent().prev().append(str_comments);
							$($this).prev().val('');
						},
						error: function (jqXHR, textStatus, errorThrown) {
							console.log(jqXHR.status);
							console.log(jqXHR.responseText);
							console.log(textStatus);
							console.log(errorThrown);
						}
					})
				}else{
					alert('댓글을 작성해주세요.');
				}
			});
			
			$('.editDiv i').click(function(){
				let className = $(this).attr('class');
				let $this = $(this);
				if(className.indexOf('trash') > 0){
					// 삭제
					let deleteConfirm = confirm('삭제하시겠습니까?'); 
					if(deleteConfirm){
						$.ajax({
							type: "DELETE",
							url: '/guestbook/delete',
							data: { 'objid' : $(this).parent().data('objid') },
							success: function (data) {
								$($this).parent().parent().parent().parent().remove();
							},
							error: function (jqXHR, textStatus, errorThrown) {
								console.log(jqXHR.status);
								console.log(jqXHR.responseText);
								console.log(textStatus);
								console.log(errorThrown);
							}
						})
					}
				}
				
			});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			console.log(jqXHR.status);
			console.debug(jqXHR.responseText);
			console.log(errorThrown);
		}
	})
};


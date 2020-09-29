/**
 * 
 */
(function ($) {
	setMemo();
}(jQuery));
function resizeGridItem(item){
  grid = document.getElementsByClassName("grid")[0];
  rowHeight = parseInt(window.getComputedStyle(grid).getPropertyValue('grid-auto-rows'));
  rowGap = parseInt(window.getComputedStyle(grid).getPropertyValue('grid-row-gap'));
  rowSpan = Math.ceil((item.querySelector('.content').getBoundingClientRect().height+rowGap)/(rowHeight+rowGap));
    item.style.gridRowEnd = "span "+rowSpan;
}

function resizeAllGridItems(){
  allItems = document.getElementsByClassName("item");
  for(x=0;x<allItems.length;x++){
    resizeGridItem(allItems[x]);
  }
}

function resizeInstance(instance){
	item = instance.elements[0];
	resizeGridItem(item);
}

window.onload = resizeAllGridItems();
window.addEventListener("resize", resizeAllGridItems);

allItems = document.getElementsByClassName("item");



function setMemo(){
	$.ajax({
		type: "POST",
		url: '/memo/getMyMemo',
		success: function (data) {
			$('div.grid').html('');
			$.each(data.memos, function(idx, i){
				const type = i.type;
				const title = i.title;
				const img = i.img;
				const desc = i.desc;
				
				let ifImg = ``;
				if(img != null && img != ''){
					ifImg = `<img class="photothumb" src="/mongoboard/${img}">`;
				}
				
				let item =  `<div class="item ${type}">
				    <div class="content">
				      <div class="title">
				        <h3>${title}</h3>
					    <div class="editMemo font-12 font-noto-sans">EDIT</div>
				      </div>
				      ${ifImg}
				      <div class="desc">
				        <p>${desc}</p>
				      </div>
				    </div>
				  </div>`;
				let $item = $(item).data(i);
				
				$('div.grid').append($item);
				
			})
			window.onload = resizeAllGridItems();
			window.addEventListener("resize", resizeAllGridItems);

			allItems = document.getElementsByClassName("item");

			$('.editMemo').click(function(){
				$('#memoEnter').addClass('d-none');
				$('#memoUpdate').removeClass('d-none');
				$('#memoDelete').removeClass('d-none');
				
				let modal = $('#MemoModal')
				let data = $(this).parent().parent().parent().data();
				
				modal.data('isNew', false);
				$('#memoType').val(data['type']);
				$('#memoTitle').val(data['title']);
				$('#memoContent').val(data['desc']);
				$('.richText-editor').html(data['desc']);
				$('.custom-file-label').html(data['img']);

				if(!($('#memoType').val() == 'photo')){
					$(".file-form").addClass("d-none");
				}else{
					$(".file-form").removeClass("d-none");
				}
				modal.data('memoObjectId',data['objectId']);
				modal.modal('show');
			})
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			console.log(jqXHR.status);
			console.debug(jqXHR.responseText);
			console.log(errorThrown);
		}
	})
}
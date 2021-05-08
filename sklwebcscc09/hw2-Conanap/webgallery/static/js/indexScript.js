(function() {
	"use strict";

	window.addEventListener('load', function() {
		let imgPage = 0;
		let currImgId = -1;
		let cmtPage = 0;
		let cmtLength = 0;
		let oldImgLen = 0;

		// updating images
		api.onImageUpdate(function(items) {
			document.querySelector('#pictures').innerHTML = '';

			if(imgPage >= items.length || oldImgLen < items.length) {
				imgPage = items.length - 1;
				oldImgLen = items.length;
			}

			let i = imgPage;
			let pic = items[i];
			if(currImgId != items[i].imageId) {
				cmtPage = 0;
				currImgId = items[i].imageId;
			}
			var ele = document.createElement('div');
			ele.className = 'indivPost';
			ele.id = pic.imageId;
			ele.innerHTML=`
			<p class="delete">x</p>
			<h2>${pic.title}</h2>
			<img src="${pic.url}" alt="Image Not Found" onerror="this.src='media/inf.png';">
			<div class="imgInfo">Posted by ${pic.author} on ${pic.date}</div>

			<div class="commentWrapper">
				<form id="commentForm" class="fillableForm">
					<input type="text" id="CAuthor" name="Author" placeholder="Author" required/>
					<textarea type="text" id="cmtArea" name="comment" placeholder="Entre something here! Press Entre while in this field to submit comment." required></textarea>
					<input type="hidden" id="CImgId" value="${pic.imageId}" />
				</form>

				<div class="comments">
				</div>
				<div class="commentNav">
					<div class="cmtNavButton" id="cNavL"><<</div>
					<div class="cmtNavButton" id="cNavR">>></div>
				</div>
			</div>
			`;
			ele.querySelector('.delete').addEventListener('click', function(e) {
				api.deleteImage(pic.imageId);
			});

			ele.querySelector('#cmtArea').addEventListener('keydown', function(e) {
				if(e.which === 13 && !e.shiftKey) {

					e.preventDefault();
					let genSelector = '#\\3' + items[i].imageId + ' .indivPost .commentWrapper #commentForm ';
					let author = document.querySelector(genSelector + '#CAuthor').value;
					let cmt = document.querySelector(genSelector + '#cmtArea').value;
					let imageId = document.querySelector(genSelector + '#CImgId').value;

					document.querySelector(genSelector).reset();

					api.addComment(imageId, author, cmt);
				}
			});

			ele.querySelector('#CAuthor').addEventListener('keydown', function(e) {
				if(e.which == 13)
					e.preventDefault();
			});

			ele.querySelector('#cNavR').addEventListener('click', function(e) {
				cmtPage++;
				if(cmtPage < 0) {
					cmtPage = cmtLength - 1;
				} else if(cmtPage >= cmtLength) {
					cmtPage = 0;
				}

				api.onLoad(cmtPage);
			});

			ele.querySelector('#cNavL').addEventListener('click', function(e) {
				cmtPage--;
				if(cmtPage < 0) {
					cmtPage = cmtLength - 1;
				} else if(cmtPage >= cmtLength) {
					cmtPage = 0;
				}

				api.onLoad(cmtPage);
			});

			// add to html
			document.getElementById('pictures').prepend(ele);


			// then updating our comments
			api.onCommentUpdate(function(items, imageId) {
				items = items || [];
				if(currImgId != imageId || imageId != ele.id) {
					return;
				}

				cmtLength = api.getTotNumCmtPages(imageId);
				let idSelector = '#\\3' + ele.id;
				let selector = idSelector + ' .indivPost .commentWrapper .comments';
				document.querySelector(selector).innerHTML = '';
				for(let i = 0; i < items.length; i++) {
					let cmt = items[i];
					let CEle = document.createElement('div');
					CEle.className = 'indivCmt';
					CEle.id = cmt.commentId;
					CEle.innerHTML=`
						<div class="cmtHdr">
							<h3>${cmt.author}</h3>
							<p class="deleteCmt">x</p>
						</div>
						<p>${cmt.content}</p>
						<p class="cmtDate">Posted on ${cmt.date}</p>
					`;

					CEle.querySelector('.deleteCmt').addEventListener('click', function(e) {
						api.deleteComment(cmt.commentId);
					});
					document.querySelector(selector).prepend(CEle);
				}
			});


		});



		// form settings for submitting a new image
		document.getElementById('addImageForm').addEventListener('submit', function(e) {
			e.preventDefault();

			let author = document.getElementById('postAuthor').value;
			let url = document.getElementById('postUrl').value;
			let title = document.getElementById('postTitle').value;

			document.getElementById('addImageForm').reset();

			api.addImage(title, author, url);
		});


		// collapse listener
		document.querySelector('#hideImgUpload').addEventListener('click', function(e) {
			let ele = document.querySelector('#hideImgUpload');
			let formDiv = document.querySelector('#addImageForm');
			let upDiv = document.querySelector('#uploadDiv');
			let curChar = ele.innerHTML;

			if(curChar === '▲') {
				ele.innerHTML = '▼';
				formDiv.style.display = 'none';
				upDiv.style.height = '5em';
			} else {
				ele.innerHTML = '▲';
				formDiv.style.display = 'flex';
				upDiv.style.height = 'auto';
			}
		});

		document.querySelector('#navR').addEventListener('click', function(e) {
			imgPage++;
			if(imgPage >= oldImgLen) {
				imgPage = 0;
			}
			api.onLoad();
		});

		document.querySelector('#navL').addEventListener('click', function(e) {
			imgPage--;
			if(imgPage < 0) {
				imgPage = oldImgLen - 1;
			}
			api.onLoad();
		});

		api.onLoad();
	});

})();
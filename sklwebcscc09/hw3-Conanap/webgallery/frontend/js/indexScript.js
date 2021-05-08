(function() {
	"use strict";

	window.addEventListener('load', function() {

		// check authenticated
		if(api.getUsername()) {
			document.querySelector('#signout').classList.remove('hidden');
		} else {
			document.querySelector('#signin').classList.remove('hidden');
		}

		let imgPage = 0;
		let cmtPage = 0;
		let cmtLength = 0;
		let imageList = [];
		let imgId = -1;
		let userPage = 0;
		let userList = [];

		let imgListener = function(pic, imgList, usrList) {
			document.querySelector('#pictures').innerHTML = '';

			imageList = imgList;
			imgId = pic.id;

			userList = usrList;

			userPage = usrList.indexOf(pic.author);

			var ele = document.createElement('div');
			ele.className = 'indivPost';
			ele.id = pic.id;
			ele.innerHTML=`
			<p class="delete">x</p>
			<h2>${pic.title}</h2>
			<img src="/api/photos/${pic.id}" alt="Image Not Found" onerror="this.src='media/inf.png';">
			<div class="imgInfo">Posted by ${pic.author} on ${pic.date}</div>

			<div class="lowerSite">
				<div id="navL" class="navButton"><<</div>
				<div id="navR" class="navButton">>></div>
			</div>

			<div class="commentWrapper">
				<form id="commentForm" class="fillableForm">
					<textarea type="text" id="cmtArea" name="comment" placeholder="Entre something here! Press Entre while in this field to submit comment." required></textarea>
					<input type="hidden" id="CImgId" value="${pic.id}" />
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
				imgPage = 0;
				api.deleteImage(pic.id);
			});

			ele.querySelector('#cmtArea').addEventListener('keydown', function(e) {
				if(e.which === 13 && !e.shiftKey) {

					e.preventDefault();
					let genSelector = '#\\3' + pic.id + ' .indivPost .commentWrapper #commentForm ';
					let cmt = document.querySelector(genSelector + '#cmtArea').value;
					let imageId = document.querySelector(genSelector + '#CImgId').value;

					document.querySelector(genSelector).reset();

					api.addComment(imageId, api.getUsername(), cmt);
				}
			});

			ele.querySelector('#cNavR').addEventListener('click', function(e) {
				cmtPage++;
				if(cmtPage > cmtLength) {
					cmtPage = 0;
				}

				api.onLoad(userList[userPage], imgId, cmtPage);
			});

			ele.querySelector('#cNavL').addEventListener('click', function(e) {
				cmtPage--;
				if(cmtPage < 0) {
					cmtPage = cmtLength;
				}

				api.onLoad(userList[userPage], imgId, cmtPage);
			});

			// add to html
			document.getElementById('pictures').prepend(ele);


			// then updating our comments
			api.onCommentUpdate(function(items) {
				items = items || [];

				cmtLength = Math.ceil((items.length) /5);
				let idSelector = '#\\3' + imgId;
				let selector = idSelector + ' .indivPost .commentWrapper .comments';
				document.querySelector(selector).innerHTML = '';
				for(let i = 0; i < items.length; i++) {
					let cmt = items[i];
					let CEle = document.createElement('div');
					CEle.className = 'indivCmt';
					CEle.id = cmt.id;
					CEle.innerHTML=`
						<div class="cmtHdr">
							<h3>${cmt.author}</h3>
							<p class="deleteCmt">x</p>
						</div>
						<p>${cmt.content}</p>
						<p class="cmtDate">Posted on ${cmt.date}</p>
					`;

					CEle.querySelector('.deleteCmt').addEventListener('click', function(e) {
						api.deleteComment(cmt.id);
					});
					document.querySelector(selector).prepend(CEle);
				}
			});

			document.querySelector('#navR').addEventListener('click', function(e) {
				imgPage++;
				if(imgPage >= imageList.length) {
					imgPage = 0;
				}
				cmtPage = 0;
				api.onLoad(userList[userPage], imageList[imgPage]);
			});
	
			document.querySelector('#navL').addEventListener('click', function(e) {
				imgPage--;
				if(imgPage < 0) {
					imgPage = imageList.length - 1;
				}
				cmtPage = 0;
				api.onLoad(userList[userPage], imageList[imgPage]);
			});

		};

		// updating images
		api.onImageUpdate(imgListener);

		// form settings for submitting a new image
		document.getElementById('addImageForm').addEventListener('submit', function(e) {
			e.preventDefault();

			imgPage = 0;

			let author = api.getUsername();
			let picture = document.getElementById('postPhoto').files[0];
			let title = document.getElementById('postTitle').value;

			document.getElementById('addImageForm').reset();

			api.addImage(title, author, picture);
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

		document.querySelector('#navRU').addEventListener('click', function(e) {
			userPage++;
			if(userPage >= userList.length) {
				userPage = 0;
			}
			cmtPage = 0;
			imgPage = 0;

			api.onLoad(userList[userPage]);
		});

		document.querySelector('#navLU').addEventListener('click', function(e) {
			userPage--;
			if(userPage < 0) {
				userPage = userList.length - 1;
			}
			cmtPage = 0;
			imgPage = 0;

			api.onLoad(userList[userPage]);
		});

		api.getUserList(function(usrList) {
			userList = usrList;
			userPage = userList.indexOf(api.getUsername());
		});
		api.onLoad(api.getUsername());
	});

})();
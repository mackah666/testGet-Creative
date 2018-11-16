// main.js


define(['./application'],


function(Application) {
	
		var defaultTheme = 'christmas';// or 'basic'

		function startApp() {

            window._APP_DEBUG = true;

			// window.ASSET_URL = '/packages/package-menu/';
			window.ASSET_URL = './content/menu/assets/';
			window.BRAND_ASSET_URL = window.ASSET_URL;
		//	window.gmi = gmi;

			var gmi = {gameContainerId:'og-game-holder'};

			var container = document.getElementById(gmi.gameContainerId) || document.body;

			var app = new Application(defaultTheme);
			container.appendChild(app.view);
			window.app = app;

			app.view.style.position = 'absolute';
			app.view.style.top = 0;
			app.view.style.left = 0;
			app.resize(window.innerWidth, window.innerHeight);

            window._APP_DEBUG =true;
            
			window.addEventListener('resize', function()
			{
				app.resize(window.innerWidth, window.innerHeight);
			});
		}
/*
			app.broadcaster.add((o)=> {
				if(o.type === 'navigate') {

					document.body.classList.add('isLoading');

					if(o.value === 'machine1') {
						console.log('Open make machine 1: ', o.assetsType);
						utils.installPackages(gmi, [`make-machine-1-assets-${o.assetsType}`, 'make-machine-1', 'make-machine-1-assets'])
						.then((packages) => {
							gmi.setGameData('hasEnteredMachine', true);
							console.log('Experience loaded. Package installed:', packages);
							document.body.classList.remove('isLoading');

							const delay = packages.length === 0 ? 0 : 1500;
							setTimeout(()=> {
								// gmi.experiences.push(o.value, {assetPath:`make-machine-1-assets-${o.assetsType}`});
								gmi.experiences.push(o.value, {assetPath:`make-machine-1-assets`, brand:o.assetsType});
							}, delay);
						}, err => {
							console.log('Error :', err);
						});

					} else if(o.value === 'machine2') {
						console.log('Open make machine : ', o.assetsType);
						utils.installPackages(gmi, [`make-machine-2-assets-${o.assetsType}`, 'make-machine-2', 'make-machine-2-assets'])
						.then((packages)=> {
							gmi.setGameData('hasEnteredMachine', true);
							console.log('Experience loaded. Package installed:', packages);
							document.body.classList.remove('isLoading');

							const delay = packages.length === 0 ? 0 : 1500;
							setTimeout(()=> {
								gmi.experiences.push(o.value, {assetPath:`make-machine-2-assets`, brand:o.assetsType});
							}, delay);

						}, err => {
							console.log('Error', err);
						});
					} else {
						gmi.experiences.push(o.value);
					}
				} else if(o.type === 'exit') {
					console.log('exit the app');
				} else if(o.type === 'setUserName') {
					console.log('Setting user name :', o.value);

					gmi.setGameData('username', o.value);
				}
			});
		}
		*/


		startApp()
	}
);
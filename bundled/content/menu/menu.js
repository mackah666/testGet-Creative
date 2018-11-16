// main.js


define(['pnm-library/gmi-mobile',
		'./application'],

	function(gmi_platform, Application) {
		"use strict";

		var defaultTheme = 'basic';

		var settingsConfig = {
			pages: [
				{
					title: "Game Settings",
					settings: [
						{
							key: "audio",
							type: "toggle",
							title: "Audio",
							description: "Turn Off/On sound and music",
						},
						{
							key: "motion",
							type: "toggle",
							title: "Motion",
							description: "Motion FX On/Off",
							defaultValue: true/false
						},
					]
				}
			]
		};

		var gmi = gmi_platform.getGMI({settingsConfig: settingsConfig});

		window.gmi = gmi;

		var gameData = gmi.getAllSettings().gameData;
		console.log('Game data :', gameData);

		var container = document.getElementById(gmi.gameContainerId);

		var buttonsContainer = document.createElement("div");
		buttonsContainer.className = "buttons-container";
		container.appendChild(buttonsContainer);

		var buttons = [];

		function addButton(label, fn)
		{
			var btn = document.createElement("BUTTON");
			btn.style.width = '200px';
			btn.style.height = '200px';
			var t = document.createTextNode(label);       // Create a text node
			btn.appendChild(t);

			buttonsContainer.appendChild(btn)

			btn.addEventListener("click", fn);
			buttons.push(btn);
		}



		document.body.style.background = '#000';

		function startApp() {
			removeBtn();

			// window.ASSET_URL = '/packages/package-menu/';
			window.ASSET_URL = gmi.packages.urlFor('menu') + 'assets/';
			window.BRAND_ASSET_URL = window.ASSET_URL;

			window.gmi = gmi;
			gmi.config.getConfigById('key-value-config/id/theme')
			.then( function( data ){
				startAppWithTheme( data.theme )
			} ) 
			.catch( function( e ) {
				console.warn('problem loading theme', e);
				startAppWithTheme(defaultTheme);
			} );
			
		}

		function startAppWithTheme( themeId )
		{
			var app = new Application( themeId );
			container.appendChild(app.view);
			window.app = app;
			app.view.style.position = 'absolute';
			app.view.style.top = 0;
			app.view.style.left = 0;
			app.resize(window.innerWidth, window.innerHeight);
		}

		function clear()
		{
			console.log('deleting..')
			buttons[1].style.visibility = 'hidden'
			window.PnMCommands.clearAll()
			.then(function(){

				buttons[1].style.visibility = 'visible'
				console.log('deleted..')
				alert("packages deleted")
			})
		}
		function removeBtn() {
			container.removeChild(buttonsContainer);

		}

		function clearUsage()
		{
			window.UsageSystem.clear()
		}

		function deleteStorage()
		{
			window.StorageManager.deleteAll();
		}

		function debugStorage()
		{
			window.DEBUG_APP_SIZE = 20 * 1000000;
			console.log('app memory size set to 20MB')
		}

		function disableBackgroundDownload()
		{
			window.AppController.allowBackgroundDownload = false;
			console.log('app background disbled')
		}

        var connectResolve = null;
        
        function canDoFirstConnection()
        {
            if(!window.gmi)return Promise.resolve(true);

            return new Promise( function(resolve) {

                if(!connectResolve)
                {
                    connectResolve = resolve;
                }
                console.log("promising..");
                
                window.gmi.config.getConfigById('order-config/id/buddy-picker-order')
                .then(function(data){
                  
                    connectResolve(true);

                })
                .catch(function(e){

                    var cb = function(){

                        canDoFirstConnection();
                    }

                    window.gmi.dialogs.showNoNetworkDialog(cb, cb, false);
                })
            })
        }


        /**
         * set to true to see activate the following:
         * show debug menu on start up
         * red wheel loader
         * tap to show version number
         * tap to show wifi status
         */
		var DEBUG = false;

		if(window.gmi.experiences.getParams().machineData)
		{
			DEBUG = false;
		}

        

		if(DEBUG)
		{
            window._APP_DEBUG = true
			addButton('clear packages', clear)
			addButton('start', function(){
                
                canDoFirstConnection().then(startApp);
            })
			addButton('clear usage', clearUsage)
			addButton('clear storage', deleteStorage)
			addButton('debug storage', debugStorage)
			addButton('disable bg download', disableBackgroundDownload)
			gmi.gameLoaded();

		}
		else
		{
            window._APP_DEBUG = false;
			canDoFirstConnection().then(startApp);

		}
	}
);

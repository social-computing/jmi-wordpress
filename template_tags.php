<?php
// ------------------------------------------------------------------------------
// Template tags definitions
// Useful to include a wps map or link to a post map in the theme where it is not possible with the plugin
// For exemple in the loop for search results or archive list
// ------------------------------------------------------------------------------
 
/**
 * Construct the html and javacript snippets to include the wps map
 * @param int $width        width of the map 
 * @param int $height       height of the map
 * @param array $flashvars  array of parameters to pass to the flash widget
 * @return string the html and javascript snippets to display a map
 */
function map($width, $height, $flashvars) { 
    $options = get_option('wpsmap_options');
    if(!isset($flashvars['wpsplanname'])) {
        $flashvars['wpsplanname'] = $options['plan_name'];
    }  
    $map = '
        <style type="text/css" media="screen"> 
            object:focus { outline:none; }
            #flashContent { display:none; }
        </style>
               
        <script type="text/javascript">
            function getMap() {
                if (navigator.appName.indexOf ("Microsoft") !=-1) {
                    return window["wps-flex"];
                }
                else {
                    return document["wps-flex"];
                }
            }
            
            function ready() {
        	// do something here
            }
            
            function status(status) {
        	// do something here
            }
            
            function error(error) {
        	alert(error);
            }


            /* Actions that are defined in swatch configuration */
            function navigate(url, target) {
        	// Nothing to do here, using native flex behavior
            }
        	 
            /* 
            // Called when an entity is selected (link = a tag here)/
	    function NewWin(args) {
	        var redirectUrl = "' . get_bloginfo("wpurl") . '/?tag=" + args[0];
	        window.location.href = redirectUrl;
                
		// Old behaviour
                // var parameters = {};
		// parameters["entityId"] = args[0];
        	// getMap().compute(parameters);
        	
	    }
	    
	    // Called when an attribute is selected (node = article here) 
	    function Discover(args) {
		var parameters = {};
		parameters["attributeId"] = args[0];
		parameters["analysisProfile"] = "DiscoveryProfile";
        	getMap().compute(parameters);
	    }
            */
        </script>
       
          
        <script type="text/javascript" src="' . plugins_url('swfobject.js', __FILE__) . '"></script>
          
          <script type="text/javascript">
              <!-- For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. --> 
              var swfVersionStr = "10.0.0";
              <!-- To use express install, set to playerProductInstall.swf, otherwise the empty string. -->
              var xiSwfUrlStr = "' . plugins_url('playerProductInstall.swf', __FILE__) . '"
              var flashvars = ' . json_encode($flashvars) . ';
              flashvars.wpsserverurl = "' . $options['server_url'] . '";
              var params = {};
              params.quality = "high";
              params.bgcolor = "#FFFFFF";
              params.allowscriptaccess = "sameDomain";
              params.allowfullscreen = "true";
              var attributes = {};
              attributes.id = "wps-flex";
              attributes.name = "wps-flex";
              attributes.align = "middle";
              swfobject.embedSWF(
                "' . plugins_url('wps-flex-1.0-SNAPSHOT.swf', __FILE__) . '", "flashContent", 
                ' . $width . ', ' . $height . ', 
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);
              <!-- JavaScript enabled so display the flashContent div in case it is not replaced with a swf object. -->

              swfobject.createCSS("#flashContent", "display:block;text-align:left;");
          </script>
        <div id="flashContent">
       	<p>
        	To view this page ensure that Adobe Flash Player version 
	    	10.0.0 or greater is installed. 
        </p>
        <script type="text/javascript"> 
    		var pageHost = ((document.location.protocol == "https:") ? "https://" :	"http://"); 
    		' . "document.write(\"<a href='http://www.adobe.com/go/getflashplayer'><img src='\" 
						   + pageHost + \"www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>\" ); 
		</script> 
        </div>" . '
        <div id="status"></div>
        <noscript>
            <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="100%" height="100%" id="wps-flex">
                <param name="movie" value="' . plugins_url('wps-flex-1.0-SNAPSHOT.swf', __FILE__) . '" />
                <param name="quality" value="high" />
                <param name="bgcolor" value="#7F9FDF" />
                <param name="allowScriptAccess" value="sameDomain" />
                <param name="allowFullScreen" value="true" />
                <!--[if !IE]>-->
                <object type="application/x-shockwave-flash" data="wps-flex-1.0-SNAPSHOT.swf" width="100%" height="100%">
                    <param name="quality" value="high" />
                    <param name="bgcolor" value="#7F9FDF" />
                    <param name="allowScriptAccess" value="sameDomain" />
                    <param name="allowFullScreen" value="true" />
                <!--<![endif]-->
                <!--[if gte IE 6]>-->
                	<p> 
                		Either scripts and active content are not permitted to run or Adobe Flash Player version
                		10.0.0 or greater is not installed.
                	</p>
                <!--<![endif]-->
                    <a href="http://www.adobe.com/go/getflashplayer">
                        <img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash Player" />
                    </a>
                <!--[if !IE]>-->
                </object>
                <!--<![endif]-->
            </object>
        </noscript>';
    return $map;
}

/**
 * Shortcut to display the result of the map function above
 * @param int $width        width of the map 
 * @param int $height       height of the map
 * @param array $flashvars  array of parameters to pass to the flash widget
 */
function the_map($width, $height, $flashvars) {
    echo map($width, $height, $flashvars);
}

/**
 * Construct a link to the map section in a post page
 * @return string a link to the map section of a post
 */
function map_link() {
    return '<div class="entry-map"><p>&#0187;<a href="' . get_permalink() . '#map">Afficher la carte à partir de cet article</a></p></div>';
}

/**
 * Shortcut to display the result of the map_link function above
 */
function the_map_link() {
    echo "<!-- the map link start -->";
    if('post' == get_post_type(get_the_ID())) {
        echo map_link();
    }
}

/**
 * Shortcut to add the "db" map javascript handler functions 
 */
function db_map_js($redirect_rel_url) {
	return '
	    <script type="text/javascript">
            /* Called when an entity is selected (link = a tag here) */
	    function NewWin(args) {
	        var redirectUrl = "' . get_bloginfo("wpurl") . $redirect_rel_url . '" + args[0];
	        window.location.href = redirectUrl;
	    }
	    
	    /* Called when an attribute is selected (node = article here) */
	    function Discover(args) {
		var parameters = {};
		parameters["attributeId"] = args[0];
		parameters["analysisProfile"] = "DiscoveryProfile";
        	getMap().compute(parameters);
	    }
            </script>
	';
}
?>

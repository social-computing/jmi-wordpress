<?php
// ------------------------------------------------------------------------------
// Template tags definitions
// Useful to include a jmi map or link to a post map in the theme where it is not possible with the plugin
// For exemple in the loop for search results or archive list
// ------------------------------------------------------------------------------
 
/**
 * Construct the html and javacript snippets to include the jmi map
 *
 * @param array    $opt the map options
 * @return string  the html and javascript snippets to display a map
 */
function jmimap($opt) { 
    // $options = get_option('jmi_options');
    // $map_options = array_merge($options, $opt);
    $map_options = getDefaultMapOptions($opt);

    $map = '
        <style type="text/css" media="screen"> 
            object:focus { outline:none; }
            #flashContent { display:none; }
        </style>
               
        <script type="text/javascript">
            function getMap() {
                if (navigator.appName.indexOf ("Microsoft") !=-1) {
                    return window["jmi-flex"];
                }
                else {
                    return document["jmi-flex"];
                }
            }
            
            function getById(id) {
                if (navigator.appName.indexOf ("Microsoft") !=-1) {
                    return window[id];
                }
                else {
                    return document.getElementById(id);
                }
            }

            
            function ready() {
        	    // do something here
            }
            
            function status(status) {
        	    // do something here
            }
            
            function error(error) {
                getById("status").innerHTML = "<h3>An error occured</h3>\n" + error;    
            }

            /* Actions that are defined in swatch configuration */
            function navigate(url, target) {
        	    // Nothing to do here, using native flex behavior
            }
        </script>
       
          
        <script type="text/javascript" src="' . plugins_url('swfobject.js', __FILE__) . '"></script>
          
        <script type="text/javascript">
             // For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection.
             var swfVersionStr = "10.0.0";
             
             // To use express install, set to playerProductInstall.swf, otherwise the empty string.
             var xiSwfUrlStr = "' . plugins_url('playerProductInstall.swf', __FILE__) . '"
             var flashvars = ' . json_encode($map_options) . ';
             var params = {};
             params.quality = "high";
             params.bgcolor = "#FFFFFF";
             params.allowscriptaccess = "sameDomain";
             params.allowfullscreen = "true";
             var attributes = {};
             attributes.id = "jmi-flex";
             attributes.name = "jmi-flex";
             attributes.align = "middle";
             swfobject.embedSWF(
                 "' . plugins_url('jmi-flex-1.0.swf', __FILE__) . '", "flashContent", 
                 ' . $map_options['width'] . ', ' . $map_options['height'] . ', 
                 swfVersionStr, xiSwfUrlStr, 
                 flashvars, params, attributes);

             // JavaScript enabled so display the flashContent div in case it is not replaced with a swf object.
             swfobject.createCSS("#flashContent", "display:block;text-align:left;");
        </script>
        <div id="status"></div>
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
        <noscript>
            <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="100%" height="100%" id="jmi-flex">
                <param name="movie" value="' . plugins_url('jmi-flex-1.0.swf', __FILE__) . '" />
                <param name="quality" value="high" />
                <param name="bgcolor" value="#7F9FDF" />
                <param name="allowScriptAccess" value="sameDomain" />
                <param name="allowFullScreen" value="true" />
                <!--[if !IE]>-->
                <object type="application/x-shockwave-flash" data="jmi-flex-1.0.swf" width="100%" height="100%">
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
 * 
 * @param $opt the map options
 */
function the_jmimap($opt) {
    echo jmimap($opt);
}

/**
 * Construct a link to the map section in a post page
  * 
 * @return string a link to the map section of a post
 */
function jmimap_link() {
    return '<div class="entry-map"><p>&#0187;<a href="' . get_permalink() . '#map">Afficher la carte à partir de cet article</a></p></div>';
}

/**
 * Shortcut to display the result of the jmimap_link function above
 */
function the_jmimap_link() {
    echo "<!-- link to the post jmi map -->";
    if('post' == get_post_type(get_the_ID())) {
        echo jmimap_link();
    }
}

/**
 * Shortcut to add the jmi map javascript handler functions 
 *
 * @param $opt the map options
 * @return the javascript snippet with default actions for the map
 */
function jmimap_js($opt) {
    $options = get_option('jmi_options');
    //$jsoptions = array_merge($options, $opt);
    $jsoptions = getDefaultMapOptions($opt);

	return '
        <script type="text/javascript"> 
            var parameters = ' . json_encode($jsoptions) . ';

            /* Called when an attribute is selected (node) */
            function DiscoverNode(args) {
                parameters.attributeId = args[0];
                parameters.analysisProfile = "DiscoveryProfile";
                getMap().compute(parameters);
            }
            
            /* Called when an entity is selected (link) */
            function DiscoverLink(args) {
                parameters.entityId = args[0];
                parameters.analysisProfile = "AnalysisProfile";
                getMap().compute(parameters);
            }

            /* Called when a user click on the display item on a node on the map */
            function DisplayNode(args) {
                var id   = args[0],
                    slug = args[1];
                var rel_url = (parameters.invert == true) ? "?p=" + id : "?tag=" + slug;
                redirect(rel_url);
            }
            
            /* Called when a user click on the display item on a node on the map */
            function DisplayLink(args) {
                var id   = args[0],
                    slug = args[1];
                var rel_url = (parameters.invert == true) ? "?tag=" + slug : "?p=" + id;
                redirect(rel_url);
            }

            function redirect(rel_url) {
                var redirectUrl = "' . get_bloginfo("wpurl") . '/" + rel_url;
                window.location.href = redirectUrl;
            }
        </script>
	';
}
?>

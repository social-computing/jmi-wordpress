<?php
/*
  Plugin Name: WPS Map
  Plugin URI:  http://www.social-computing.com/
  Description: A Map plugin querying WPS server for Wordpress 3.0 or above displaying relationships between posts and tags or categories.
  Version:     0.2
  Author:      Jonathan Dray
  Author URI:  http://www.social-computing.com/
  Copyright:   Copyright (c) 2011 Social Computing 
*/


// ------------------------------------------------------------------------
// PLUGIN PREFIX:                                                          
// ------------------------------------------------------------------------
// A PREFIX IS USED TO AVOID CONFLICTS WITH EXISTING PLUGIN FUNCTION NAMES.
// WHEN CREATING A NEW PLUGIN, CHANGE THE PREFIX AND USE YOUR TEXT EDITORS 
// SEARCH/REPLACE FUNCTION TO RENAME THEM ALL QUICKLY.                     
// ------------------------------------------------------------------------
// 'wpsmap_' prefix is chosen for this plugin

// ------------------------------------------------------------------------
// REGISTER HOOKS & CALLBACK FUNCTIONS:                                    
// ------------------------------------------------------------------------
// HOOKS TO SETUP DEFAULT PLUGIN OPTIONS, HANDLE CLEAN-UP OF OPTIONS WHEN
// PLUGIN IS DEACTIVATED AND DELETED, INITIALISE PLUGIN, ADD OPTIONS PAGE.
// ------------------------------------------------------------------------
// Set-up Hooks
register_activation_hook(__FILE__, 'wpsmap_add_defaults');
register_uninstall_hook(__FILE__, 'wpsmap_delete_plugin_options');

if (is_admin()) require_once dirname( __FILE__ ) . '/admin.php';
require_once dirname( __FILE__ ) . '/template_tags.php';


/**
 * Define default option settings
 *
 * CALLBACK FUNCTION FOR: register_activation_hook(__FILE__, 'wpsmap_add_defaults')
 * ---------------------------------------------------------------------------

 * THIS FUNCTION RUNS WHEN THE PLUGIN IS ACTIVATED. IF THERE ARE NO THEME OPTIONS
 * CURRENTLY SET, OR THE USER HAS SELECTED THE CHECKBOX TO RESET OPTIONS TO THEIR
 * DEFAULTS THEN THE OPTIONS ARE SET/RESET.
 *
 * OTHERWISE, THE PLUGIN OPTIONS REMAIN UNCHANGED.
 * ---------------------------------------------------------------------------
 */ 
function wpsmap_add_defaults() {
	$tmp = get_option('wpsmap_options');
    if(($tmp['chk_default_options_db']=='1')||(!is_array($tmp))) {
		delete_option('wpsmap_options'); // so we don't have to reset all the 'off' checkboxes too! (don't think this is needed but leave for now)
		$arr = array(	"chk_button1" => "1",
						"chk_button3" => "1",
						"textarea_one" => "This type of control allows a large amount of information to be entered all at once. Set the 'rows' and 'cols' attributes to set the width and height.",
						"txt_one" => "Enter whatever you like here..",
						"drp_select_box" => "four",
						"chk_default_options_db" => "",
						"rdo_group_one" => "one",
						"rdo_group_two" => "two"
		);
		update_option('wpsmap_options', $arr);
	}
}

/**
 * Delete options table entries ONLY when plugin deactivated AND deleted
 *
 * CALLBACK FUNCTION FOR: register_uninstall_hook(__FILE__, 'wpsmap_delete_plugin_options')
 * ---------------------------------------------------------------------------
 * THIS FUNCTION RUNS WHEN THE USER DEACTIVATES AND DELETES THE PLUGIN. IT SIMPLY DELETES
 * THE PLUGIN OPTIONS DB ENTRY (WHICH IS AN ARRAY STORING ALL THE PLUGIN OPTIONS).
 * ---------------------------------------------------------------------------
 */ 
function wpsmap_delete_plugin_options() {
	delete_option('wpsmap_options');
}


/**
 * Display a Settings link on the main Plugins page
 * @param array $links
 * @param string $file
 * @return an array of links
 */
function wpsmap_plugin_action_links($links, $file) {
	if ($file == plugin_basename( __FILE__ )) {
		$wpsmap_links = '<a href="' . get_admin_url() . 'options-general.php?page=wpsmap/admin.php">'.__('Settings').'</a>';
		// make the 'Settings' link appear first
		array_unshift($links, $wpsmap_links );
	}
	return $links;
}

// plugin_action_links filter hook to display settings link
// Calls the function above 'wpsmap_plugin_action_links'
add_filter('plugin_action_links', 'wpsmap_plugin_action_links', 10, 2);
    

/**
 * Add the map to the post content
 * @param string $post_content the original post content
 * @return the post content with map included
 */
function post_display($post_content) {
    // Decide whether or not to generate the map.
    // For now, only if it is a single post 
    
    // Read plugin options
    $options = get_option('wpsmap_options');
    $the_post = $post_content;
    
    if (is_single()) {
        $width  = $options['width'];
        $height = $options['height'];
        global $wp_query;
        $thePostID = $wp_query->post->ID;
        $the_post .= '<div><h2 id="map">Articles connexes</h2>';
        $the_post .= map($width, $height,  array('analysisProfile' => 'DiscoveryProfile', 'attributeId' => $wp_query->post->ID));
        $the_post .= '</div>';
    }
    return $the_post ;
}

// the_content action hook called to modify a post content to add a map 
// Calls the function above 'post_display'
add_action('the_content', 'post_display'); 
?>

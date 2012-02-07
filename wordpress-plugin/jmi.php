<?php
/*
  Plugin Name: Just Map It!
  Plugin URI:  http://www.social-computing.com/
  Description: Interactive map plugin, displaying relationships between posts and tags or categories.
  Version:     1.0
  Author:      Jonathan Dray
  Author URI:  http://www.social-computing.com/
  Copyright:   Copyright (c) 2011 - 2012 Social Computing 
*/


// ------------------------------------------------------------------------
// PLUGIN PREFIX:                                                          
// ------------------------------------------------------------------------
// A PREFIX IS USED TO AVOID CONFLICTS WITH EXISTING PLUGIN FUNCTION NAMES.
// WHEN CREATING A NEW PLUGIN, CHANGE THE PREFIX AND USE YOUR TEXT EDITORS 
// SEARCH/REPLACE FUNCTION TO RENAME THEM ALL QUICKLY.                     
// ------------------------------------------------------------------------
// 'jmi_' prefix is chosen for this plugin

// ------------------------------------------------------------------------
// REGISTER HOOKS & CALLBACK FUNCTIONS:                                    
// ------------------------------------------------------------------------
// HOOKS TO SETUP DEFAULT PLUGIN OPTIONS, HANDLE CLEAN-UP OF OPTIONS WHEN
// PLUGIN IS DEACTIVATED AND DELETED, INITIALISE PLUGIN, ADD OPTIONS PAGE.
// ------------------------------------------------------------------------
// Set-up Hooks
register_activation_hook(__FILE__, 'jmi_add_defaults');
register_uninstall_hook(__FILE__, 'jmi_delete_plugin_options');

if (is_admin()) require_once dirname( __FILE__ ) . '/admin.php';
require_once dirname( __FILE__ ) . '/template_tags.php';


/**
 * Define default option settings
 *
 * Callback function for {@link register_activation_hook(__FILE__, 'jmi_add_defaults')}
 * 
 * THIS FUNCTION RUNS WHEN THE PLUGIN IS ACTIVATED. IF THERE ARE NO THEME OPTIONS
 * CURRENTLY SET, OR THE USER HAS SELECTED THE CHECKBOX TO RESET OPTIONS TO THEIR
 * DEFAULTS THEN THE OPTIONS ARE SET/RESET.
 *
 * OTHERWISE, THE PLUGIN OPTIONS REMAIN UNCHANGED.
 */ 
function jmi_add_defaults() {
	$tmp = get_option('jmi_options');
    if(($tmp['chk_default_options_db']=='1') || (!is_array($tmp))) {
		// so we don't have to reset all the 'off' checkboxes too! (don't think this is needed but leave for now)
		delete_option('jmi_options');
		$arr = array(	"chk_button1" => "1",
						"chk_button3" => "1",
						"textarea_one" => "This type of control allows a large amount of information to be entered all at once. Set the 'rows' and 'cols' attributes to set the width and height.",
						"txt_one" => "Enter whatever you like here..",
						"drp_select_box" => "four",
						"chk_default_options_db" => "",
						"rdo_group_one" => "one",
						"rdo_group_two" => "two"
		);
		update_option('jmi_options', $arr);
	}
}

/**
 * Delete options table entries ONLY when plugin deactivated AND deleted
 *
 * CALLBACK FUNCTION FOR: register_uninstall_hook(__FILE__, 'jmi_delete_plugin_options')
 * ---------------------------------------------------------------------------
 * THIS FUNCTION RUNS WHEN THE USER DEACTIVATES AND DELETES THE PLUGIN. IT SIMPLY DELETES
 * THE PLUGIN OPTIONS DB ENTRY (WHICH IS AN ARRAY STORING ALL THE PLUGIN OPTIONS).
 * ---------------------------------------------------------------------------
 */ 
function jmi_delete_plugin_options() {
	delete_option('jmi_options');
}


/**
 * Display a Settings link on the main Plugins page
 *
 * @param array $links
 * @param string $file
 *
 * @return an array of links
 */
function jmi_plugin_action_links($links, $file) {
	if ($file == plugin_basename( __FILE__ )) {
		$jmi_links = '<a href="' . get_admin_url() . 'options-general.php?page=jmi/admin.php">'.__('Settings').'</a>';
		// make the 'Settings' link appear first
		array_unshift($links, $jmi_links );
	}
	return $links;
}

// plugin_action_links filter hook to display settings link
// Calls the function above 'jmi_plugin_action_links'
add_filter('plugin_action_links', 'jmi_plugin_action_links', 10, 2);
    

/**
 * Add the map to the post content
 *
 * @param string $post_content the original post content
 *
 * @return the post content with map included
 */
function post_display($post_content) {
    // Decide whether or not to generate the map.
    // For now, only if it is a single post 
    
    // Read plugin options
    $options = get_option('jmi_options');
    $the_post = $post_content;

    // Post map specific options
    global $wp_query;
    $map_options = array('analysisProfile' => 'DiscoveryProfile', 'attributeId' => $wp_query->post->ID, 'invert' => true);
    
    if (is_single() && $options['map_on_posts']) {
        $the_post .= '<div><h2 id="map">Articles connexes</h2>';
        $the_post .= jmimap_js($map_options);
        $the_post .= jmimap($map_options);
        $the_post .= '</div>';
    }
    return $the_post;
}

// the_content action hook called to modify a post content to add a map 
// Calls the function above 'post_display'
add_action('the_content', 'post_display'); 


// ------------------------------------------------------------------------
// JMI REST Controller registration
// ------------------------------------------------------------------------
function add_jmi_rest_controller($controllers) {
    $controllers[] = 'jmi';
    return $controllers;
}
add_filter('json_api_controllers', 'add_jmi_rest_controller');

function set_jmi_controller_path() {
    return plugin_dir_path(__FILE__) .  'jmi-rest.php';
}
add_filter('json_api_jmi_controller_path', 'set_jmi_controller_path');


// ------------------------------------------------------------------------
// UTILITY FUNCTIONS
// ------------------------------------------------------------------------

/**
 * Get plugin default values for the jmi client
 *
 * @param array   values of options actually set
 * @return array  the default plugin values for the jmi client merged with the
 *                given options
 */
function getDefaultMapOptions($opt) {
    $opt_list = array("wpsserverurl", "wpsplanname", "wordpressurl", "height", "width");
    $options = array_intersect_key(get_option('jmi_options'), 
                                   array_flip($opt_list));
    return array_merge($options, $opt);
}


/*
 * Filters the selected facets to match the given type
 *
 * @param $facets an array of selected facets
 * @param $type a facet type (could be tags, categories, authors...)
 * 
 * @return an array of selected facets matching the given type
 */
function getSelectedFacets($facets, $type) {
	$selectedTags = array();
	if ($facets) {
		foreach ($facets as $selectedfacet) {
		$splititm = split(':', $selectedfacet["name"], 2);
                	if($splititm[0] == $type) {
				$selectedTags[$splititm[1]] = html_entity_decode($selectedfacet['removelink']);
                        }
                }
        }
	return $selectedTags;
}

/*
 * Get all the available facets for a given type
 * 
 * @param $facets array of all facets as returned by the solr results object 
 * @param $type a facet type (ie tags, categories, authors, ...)
 * 
 * @return an array of facets available for one type and one query
 */
function getAvailableFacets($facets, $type) {
	$availableFacets = array();
	if ($facets) {
		foreach ($facets as $facet) {
			if($facet['name'] == $type) {
				foreach ($facet['items'] as $item) {
					$availableFacets[$item['name']] = html_entity_decode($item['link']);
				}
				// return $facet['items'];
			}
		}
	}
	return $availableFacets;	
}
?>

<?php
/**
 * Administration section of the Just Map It! plugin
 *
 * @package jmi
 * @author Jonathan Dray <jonathan@social-computing.com>
 * @version 1.0
 */

/*
 * Hooks for the plugin administration page and menu initialization
 */
add_action('admin_init', 'jmi_admin_init');
add_action('admin_menu', 'jmi_add_options_page');

/**
 * Init plugin options to white list our options
 *
 * CALLBACK FUNCTION FOR: add_action('admin_init', 'jmi_admin_init' )
 * ---------------------------------------------------------------------------
 * THIS FUNCTION RUNS WHEN THE 'admin_init' HOOK FIRES, AND REGISTERS YOUR PLUGIN
 * SETTING WITH THE WORDPRESS SETTINGS API. YOU WON'T BE ABLE TO USE THE SETTINGS
 * API UNTIL YOU DO.
 * ---------------------------------------------------------------------------
 */ 
function jmi_admin_init(){
	register_setting('jmi_plugin_options', 'jmi_options', 'jmi_validate_options');
}


/**
 * Add menu page
 *
 * CALLBACK FUNCTION FOR: add_action('admin_menu', 'jmi_add_options_page');
 * ---------------------------------------------------------------------------
 * THIS FUNCTION RUNS WHEN THE 'admin_menu' HOOK FIRES, AND ADDS A NEW OPTIONS
 * PAGE FOR YOUR PLUGIN TO THE SETTINGS MENU.
 * ---------------------------------------------------------------------------
 */ 
function jmi_add_options_page() {
	add_options_page('Just Map It! options page', 'Just Map It!', 'manage_options', __FILE__, 'jmi_render_form');
}


/**
  * Render the Plugin options form
  *
  * CALLBACK FUNCTION SPECIFIED IN: add_options_page()
  * ---------------------------------------------------------------------------
  * THIS FUNCTION IS SPECIFIED IN add_options_page() AS THE CALLBACK FUNCTION THAT
  * ACTUALLY RENDER THE PLUGIN OPTIONS FORM AS A SUB-MENU UNDER THE EXISTING
  * SETTINGS ADMIN MENU.
  * ---------------------------------------------------------------------------
  */ 
function jmi_render_form() {
	?>
	<div class="wrap">
		
		<!-- Display Plugin Icon, Header, and Description -->
		<div class="icon32" id="icon-options-general"><br></div>
		<h2>Just Map It!</h2>

		<!-- Beginning of the Plugin Options Form -->
		<form method="post" action="options.php">
			<?php settings_fields('jmi_plugin_options'); ?>
			<?php $options = get_option('jmi_options'); ?>

			<!-- Table Structure Containing Form Controls -->
			<!-- Each Plugin Option Defined on a New Table Row -->
			<table class="form-table">
				<!-- Textbox Controls -->
				<tr>
					<th scope="row">Server URL</th>
					<td>
						<input type="text" size="100" name="jmi_options[wpsserverurl]" value="<?php echo $options['wpsserverurl']; ?>" />
					</td>
				</tr>
				<tr>
					<th scope="row">Wordpress REST service URL</th>
					<td>
						<input type="text" size="100" name="jmi_options[wordpressurl]" value="<?php echo $options['wordpressurl']; ?>" />
					</td>
				</tr>
				<tr>
					<th scope="row">Plan name</th>
					<td>
						<input type="text" size="50" name="jmi_options[wpsplanname]" value="<?php echo $options['wpsplanname']; ?>" />
					</td>
				</tr>
				<tr>
					<th scope="row">Width</th>
					<td>
						<input type="text" size="4" name="jmi_options[width]" value="<?php echo $options['width']; ?>" />
					</td>
				</tr>
				<tr>
					<th scope="row">Height</th>
					<td>
						<input type="text" size="4" name="jmi_options[height]" value="<?php echo $options['height']; ?>" />
					</td>
				</tr>
				<tr>
					<th scope="row">Login</th>
					<td>
						<input type="text" size="15" name="jmi_options[login]" value="<?php echo $options['login']; ?>" />
					</td>
				</tr>
				<tr>
					<th scope="row">Password</th>
					<td>
						<input type="password" name="jmi_options[password]" value="<?php echo $options['password']; ?>" autocomplete="off" />
					</td>
				</tr>

				<tr><td colspan="2"><div style="margin-top:10px;"></div></td></tr>
				<tr valign="top" style="border-top:#dddddd 1px solid;">
					<th scope="row">Map positioning</th>
					<td>
						<label><input name="jmi_options[map_on_posts]" type="checkbox" value="1" <?php if (isset($options['map_on_posts'])) { checked('1', $options['map_on_posts']); } ?> /> Display the map at the bottom of a post</label>
						<br /><span style="color:#666666;margin-left:2px;">Check this to add a map at the bottom of your blog posts.</span>
					</td>
				</tr>
				
				<tr><td colspan="2"><div style="margin-top:10px;"></div></td></tr>
				<tr valign="top" style="border-top:#dddddd 1px solid;">
					<th scope="row">Database Options</th>
					<td>
						<label><input name="jmi_options[chk_default_options_db]" type="checkbox" value="1" <?php if (isset($options['chk_default_options_db'])) { checked('1', $options['chk_default_options_db']); } ?> /> Restore defaults upon plugin deactivation/reactivation</label>
						<br /><span style="color:#666666;margin-left:2px;">Only check this if you want to reset plugin settings upon Plugin reactivation</span>
					</td>
				</tr>
			</table>
			<p class="submit">
			<input type="submit" class="button-primary" value="<?php _e('Save Changes') ?>" />
			</p>
		</form>
	</div>
	<?php	
}


/**
 * Sanitize and validate input.
 *
 * @param array $input
 * @return array a sanitized input array
 */
function jmi_validate_options($input) {
	// Sanitize textbox input (strip html tags, and escape characters)
	$input['wpsserverurl'] =  wp_filter_nohtml_kses($input['wpsserverurl']);
	$input['wpsplanname'] =  wp_filter_nohtml_kses($input['wpsplanname']);
	$input['wordpressurl'] =  wp_filter_nohtml_kses($input['wordpressurl']);
	$input['width'] =  wp_filter_nohtml_kses($input['width']); 
	$input['height'] =  wp_filter_nohtml_kses($input['height']);  
	$input['login'] =  wp_filter_nohtml_kses($input['login']); 
	$input['password'] =  wp_filter_nohtml_kses($input['password']);  
	return $input;
}
?>

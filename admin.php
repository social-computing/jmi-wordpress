<?php

add_action('admin_init', 'wpsmap_admin_init');
add_action('admin_menu', 'wpsmap_add_options_page');

/**
 * Init plugin options to white list our options
 *
 * CALLBACK FUNCTION FOR: add_action('admin_init', 'wpsmap_admin_init' )
 * ---------------------------------------------------------------------------
 * THIS FUNCTION RUNS WHEN THE 'admin_init' HOOK FIRES, AND REGISTERS YOUR PLUGIN
 * SETTING WITH THE WORDPRESS SETTINGS API. YOU WON'T BE ABLE TO USE THE SETTINGS
 * API UNTIL YOU DO.
 * ---------------------------------------------------------------------------
 */ 
function wpsmap_admin_init(){
	register_setting('wpsmap_plugin_options', 'wpsmap_options', 'wpsmap_validate_options');
}


/**
 * Add menu page
 *
 * CALLBACK FUNCTION FOR: add_action('admin_menu', 'wpsmap_add_options_page');
 * ---------------------------------------------------------------------------
 * THIS FUNCTION RUNS WHEN THE 'admin_menu' HOOK FIRES, AND ADDS A NEW OPTIONS
 * PAGE FOR YOUR PLUGIN TO THE SETTINGS MENU.
 * ---------------------------------------------------------------------------
 */ 
function wpsmap_add_options_page() {
	add_options_page('WPS Map options page', 'WPS Map', 'manage_options', __FILE__, 'wpsmap_render_form');
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
function wpsmap_render_form() {
	?>
	<div class="wrap">
		
		<!-- Display Plugin Icon, Header, and Description -->
		<div class="icon32" id="icon-options-general"><br></div>
		<h2>WPS Map</h2>

		<!-- Beginning of the Plugin Options Form -->
		<form method="post" action="options.php">
			<?php settings_fields('wpsmap_plugin_options'); ?>
			<?php $options = get_option('wpsmap_options'); ?>

			<!-- Table Structure Containing Form Controls -->
			<!-- Each Plugin Option Defined on a New Table Row -->
			<table class="form-table">
				<!-- Textbox Controls -->
				<tr>
					<th scope="row">Server URL</th>
					<td>
						<input type="text" size="100" name="wpsmap_options[server_url]" value="<?php echo $options['server_url']; ?>" />
					</td>
				</tr>
				<tr>
					<th scope="row">Plan name</th>
					<td>
						<input type="text" size="50" name="wpsmap_options[plan_name]" value="<?php echo $options['plan_name']; ?>" />
					</td>
				</tr>
				<tr>
					<th scope="row">Width</th>
					<td>
						<input type="text" size="4" name="wpsmap_options[width]" value="<?php echo $options['width']; ?>" />
					</td>
				</tr>
				<tr>
					<th scope="row">Height</th>
					<td>
						<input type="text" size="4" name="wpsmap_options[height]" value="<?php echo $options['height']; ?>" />
					</td>
				</tr>

				
				<tr><td colspan="2"><div style="margin-top:10px;"></div></td></tr>
				<tr valign="top" style="border-top:#dddddd 1px solid;">
					<th scope="row">Database Options</th>
					<td>
						<label><input name="wpsmap_options[chk_default_options_db]" type="checkbox" value="1" <?php if (isset($options['chk_default_options_db'])) { checked('1', $options['chk_default_options_db']); } ?> /> Restore defaults upon plugin deactivation/reactivation</label>
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
 * @param array $input
 * @return a sanitized array
 */
function wpsmap_validate_options($input) {
	// Sanitize textbox input (strip html tags, and escape characters)
	$input['server_url'] =  wp_filter_nohtml_kses($input['server_url']);
	$input['width'] =  wp_filter_nohtml_kses($input['width']); 
	$input['height'] =  wp_filter_nohtml_kses($input['height']);  
	return $input;
}
?>

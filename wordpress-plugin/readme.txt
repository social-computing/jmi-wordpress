=== Just Map It! map for Wordpress ===
Tags: jmi, map, dynamic, interactive
Contributors: Jonathan Dray
Requires at least: 3.0
Tested up to: 3.3.1
Stable tag: 1.0

== Description ==

Just Map It! plugin display a map on wordpress pages, linking posts by their common tags.  

== Installation ==

= Requirements = 

This plugin depend on the JSON API Plugin available at http://wordpress.org/extend/plugins/json-api/ 
Please follow the installation steps of the that plugin and check that it is
enabled before trying to install this plugin.

= Installation steps =

1. Put the jmi directory into [wordpress_dir]/wp-content/plugins/
2. Go into the WordPress admin interface and activate the plugin
3. Open the JSON API plugin configuration page and enable the JMI controller.
4. Open the Just Map It! configuration page and 

== Configuration ==

= Global options = 

 * Server url: location of the Just Map It! server. For now only one value is
   allowed : http://server.just-map-it.com

 * Wordpress REST service URL: the location of your wordress REST service that 
   exposes 

 * Height and width: dimension of the map.

By default the map is disabled, check the display map on posts options after
the global options have been set.

= Map on a tag or a category archive page =

To add the map on the tag archive and category archive pages, paste the
following snippets, in the following files in your template directory : 

 * tag.php

<code>
    <?php if(function_exists('jmimap')) : ?>
    <?php
        $map_options = array('width' => '650', 
                             'height' => '400',
                             'analysisProfile' => 'DiscoveryProfile', 
                             'attributeId' => get_query_var('tag_id'), 
                             'invert' => false);
        echo jmimap_js($map_options); 
        the_jmimap($map_options);
    ?>
    <?php endif; ?>
</code>

 * category.php

<code>
    <?php if(function_exists('jmimap')) : ?>
    <?php                
        $map_options = array('width' => '650',
                             'height' => '400',
                             'analysisProfile' => 'GlobalProfile', 
                             'invert' => true, 
                             'wordpressurl' => 'http://blog.maka/api/jmi/category_posts?id=' . get_query_var('cat'));
        echo jmimap_js($map_options); 
        the_jmimap($map_options);
    ?>
    <?php endif; ?>
</code>

== Frequently Asked Questions ==

= How can I tell if it's working? =

A interactive map should appear at the bottom of each blog post when the option
is checked. This map show the relation between the post being read and other
blog posts by the tags they share.

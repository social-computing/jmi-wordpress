=== Just Map It! map for Wordpress ===
Tags: jmi, map, dynamic, interactive
Contributors: Jonathan Dray
Requires at least: 3.0
Tested up to: 3.3.1
Stable tag: 1.0

== Description ==

Just Map It! Wordpress : this plugin enables the visualization of a Wordpress posts map. Posts are positioned in relation to the shared keywords. The map is a navigation tool : allowing you to center on any post and to discover related contents around this post. We applied this plugin to our News stream. Just try it!

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

 * Login and password: login info to provide to access the blog rest service if
   the access is restricted (disable by default)

By default the map is disabled, check the 'display map on posts' options after
the global options have been set.

= Map on a tag or a category archive page =

To add the map on the tag archive and category archive pages, paste the
following snippets, in the following files in your template directory : 

 * tag.php

<code>
    <?php if(function_exists('jmimap')) : ?>
    <?php
        $tag_id = get_query_var('tag_id');
        $options = get_option('jmi_options');
        $wpurl = $options['wordpressurl'] . '/tag_posts?id=' . $tag_id;
        $map_options = array('width' => '650', 
                             'height' => '400',
                             'analysisProfile' => 'DiscoveryProfile', 
                             'attributeId' => $tag_id, 
                             'invert' => false,
                             'wordpressurl' => $wpurl);
        echo jmimap_js($map_options); 
        the_jmimap($map_options);
    ?>
    <?php endif; ?>
</code>

 * category.php

<code>
    <?php if(function_exists('jmimap')) : ?>
    <?php                
        $options = get_option('jmi_options');
        $wpurl = $options['wordpressurl'] . '/category_posts?id=' . get_query_var('cat');
        $map_options = array('width' => '650',
                             'height' => '400',
                             'analysisProfile' => 'GlobalProfile', 
                             'invert' => true, 
                             'wordpressurl' => $wpurl);
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


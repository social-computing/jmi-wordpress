<?php
/*
Controller name: JMI
Controller description: Controller specific to Just Map It! extension
*/
class JSON_API_JMI_Controller {
    public function posts() {
        global $json_api;
        
        // See also: http://codex.wordpress.org/Template_Tags/query_posts
        $results = $json_api->introspector->get_posts(array('posts_per_page' => 50));
        return $this->getPostsAndTags($results);
    }
    
    public function last_posts() {
        global $json_api;
        
        $id = $json_api->query->get('id');
        $max = $json_api->query->get('max');
	$max = (!$max) ? 50 : $max; 

        // Get post by id
        if ($id) {
	    $results = get_posts(array('p' => $id));
            if(!$results) {
                $json_api->error("Post not found.");
            }
            $post = new JSON_API_Post($results[0]);
        } 

        // Get the last ($max) posts
	$posts = $json_api->introspector->get_posts(
            array('posts_per_page' => $max,
                  'post__not_in' => array($id)
                 )
        );
        
        // Add the current post to the list  
	$posts[] = $post;

        return $this->getPostsAndTags($posts);
    }
    
    public function category_posts() {
        global $json_api;
        $category = $json_api->introspector->get_current_category();
        if (!$category) {
            $json_api->error("Not found.");
        }
        
        $results = $json_api->introspector->get_posts(
            array(
                'cat' => $category->id
            )
        );
        return $this->getPostsAndTags($results);
    }
   
    public function tag_posts() {
        global $json_api;
        $tag = $json_api->introspector->get_current_tag();
        if (!$tag) {
            $json_api->error("Not found.");
        }
        $results = $json_api->introspector->get_posts(
            array('tag' => $tag->slug)
        );
        return $this->getPostsAndTags($results);
    }

    /**
     * Get posts that share the same tags as the given one.
     */
    public function related_posts() {
        global $json_api;
        $id = $json_api->query->get('id');

        if ($id) {
	    $results = get_posts(array('p' => $id));
            if(!$results) {
                $json_api->error("Post not found.");
            }
            $post = new JSON_API_Post($results[0]);
        } 
        else {
            $json_api->error("Include 'id' var in your request.");
        }
	
	// Get all tags from the current post
        foreach($results[0]->tags as $tag) {
	    $tags_id[] = $tag->term_id;
	}

        // Prepare query arguments and search posts containing the same tags
        // as the current post
        $posts = $json_api->introspector->get_posts(
            array('tag__in' => $tags_id,
                  'post__not_in' => array($id),
                  'posts_per_page' => 50)
	);
        if(!$posts) $posts = array();

	// Adding the current post to the list
	$posts[] = $post;

        return $this->getPostsAndTags($posts);
    }


    /**
     * Extract posts and tags informations from the result provided.
     * <p>
     * Utility method to construct the result for the Just Map It! Server.
     * </p>
     *
     * @param result  a an object containing posts after a query on the WP db
     * @return        an array containg the posts and tags in a correct format
     *                the jmi REST connector.
     */
    protected function getPostsAndTags($result){
        $posts = array();
        $tags = array();
        
        foreach ($result as $post) {
            $tags_id = array();
            foreach ($post->tags as $tag) {
                if (!isset($tags[$tag->id])) {
                    $tags[$tag->id] = array(
                        'id' => "$tag->id",
                        'slug' => $tag->slug,
                        'title' => $tag->title,
                        'description' => $tag->description
                    );
                }
                $tags_id[] = array(
                    'id' => "$tag->id"
                );
            }
            $posts[] = array(
                'id' => "$post->id",
                'slug' => $post->slug,
                'url' => $post->url,
                'title' => $post->title,
                'tags' => $tags_id
            );
        }
        return array(
            'posts' => $posts,
            'tags' => array_values($tags)
        );
    }
}
?>

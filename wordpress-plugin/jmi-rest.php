<?php
/*
Controller name: JMI
Controller description: Controller specific to Just Map It! extension
*/
class JSON_API_JMI_Controller {
    public function posts() {
        global $json_api;
        
        // See also: http://codex.wordpress.org/Template_Tags/query_posts
        $results = $json_api->introspector->get_posts();
        return $this->getPostsAndTags($results);
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

        //return $this->posts_object_result($posts, $category);
        return $this->getPostsAndTags($results);
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

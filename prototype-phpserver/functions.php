<?php

function leo_connect($user, $pass, $url) {
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_UNRESTRICTED_AUTH, true);
    curl_setopt($ch, CURLOPT_AUTOREFERER, true);
    curl_setopt($ch, CURLOPT_HEADER, 0);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_NTLM);
    curl_setopt($ch, CURLOPT_USERPWD, $user . ":" . $pass);
    $output = curl_exec($ch);
    curl_close($ch);
    return $output;
}

function json_connect($array) {
    $json = json_encode($array);
    if (preg_match('|null|', $json)){
        return 'invalid';
    }
    else if ($json == '[]'){
        return 'invalid';
    }
    else {
        return $json;
    }
}

function html_filter($output, $filter) {
    $doc = new DOMDocument();
    libxml_use_internal_errors(true);
    $doc->loadHTML('<?xml encoding="UTF-8">' . $output);
    return $doc->getElementsByTagName($filter);
}

?>

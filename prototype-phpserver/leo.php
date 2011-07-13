<?php

include 'functions.php';
include 'simple_html_dom.php';

if (!isset($_POST['mode'])) die("invalid");

$user = $_POST['user'];
$pass = $_POST['pass'];
$mode = $_POST['mode'];
if (isset($user) || $user != "") {
    if ($mode == 'timetable') {
        $url = ("http://leo.rp.edu.sg//workspace/timetable.asp");
        $output = leo_connect($user, $pass, $url);
        $docs = html_filter($output, 'td');
        $tempDate;
        $masterClass = array();
        foreach ($docs as $nodes) {
            $nodeValue = $nodes->nodeValue;
            $class = array();
            $regex = array('|[A-Z]{1}[\d]{3}-[\d]{1}-[\w]{1}[\d]{2}[\w]{1}-[A-Z]{1}|',
                '|Venue: [\w]{4}|',
                '|Problem: [\d]{2}|',
                '|Time: [\d]{2}\:[\d]{2}|');
            $dateRegex = '|[\d]{1,2}/[\d]{1,2}[\s]{1}\([\w]{3}\)|';
            if (preg_match($regex[0], $nodeValue)) {
				$class["date"] = $tempDate;
                foreach ($regex as $i => $code) {
                    preg_match_all($code, $nodes->nodeValue, $return);
                    $result = $return[0][0];
					$key = "id";
					if ($i > 0) {
						$findkey = explode(": ", $result);
						$key = strtolower($findkey[0]);
						$result = $findkey[1];
						if ($key == "time") {
							if (substr($result, 0, 1) == "0") $result = substr($result, 1, strlen($result));
						}
					}
					$class[$key] = $result;
                    $nodeValue = preg_replace($regex, "", $nodeValue);
                }
                $nodeValue = preg_replace('|Day: [\d]{1}|', "", $nodeValue);
				$class["title"] = $nodeValue;
                array_push($masterClass, $class);
            } else if (preg_match($dateRegex, $nodeValue)) {
                $tempDate = $nodeValue;
            }
        }
		$masterClass = array_reverse($masterClass);
        echo json_connect($masterClass);
    } else if ($mode == 'ut') {
        $url = "http://leo.rp.edu.sg//workspace/UT3timetable.asp";
        $output = leo_connect($user, $pass, $url);
        $replace = array('<small>', '</small>', '<i>', '</i>');
        foreach ($replace as $r) {
            $output = str_replace($r, "", $output);
        }
        $regex = array('|[\d]{1,2}\/[\d]{1,2}\/[\d]{4}[\s]{1}\([\w]{3}\)|',
            '|[A-Z]{1}[\d]{3} Understanding Test [\d]{1}|',
            '|[A-Z]{1}[\d]{2}[A-Z]{1}|',
            '|[\d]{2}:[\d]{2}|');
        $sort = array();
        foreach ($regex as $params) {
            preg_match_all($params, $output, $matchesarray);
            array_push($sort, $matchesarray);
        }
        $result = array();
        for ($i = 0; $i < count($sort[0][0]); $i++) {
			$time_array = explode(":", $sort[3][0][$i]);
			$hour = intval($time_array[0]);
			if ($hour > 12) { $hour = $hour - 12; }
			$time = $hour . ":" . $time_array[1];
            $tempArray = array("date" => $sort[0][0][$i], "title" => $sort[1][0][$i], "venue" => $sort[2][0][$i], "time" => $time);
            array_push($result, $tempArray);
        }
		$result = array_reverse($result);
        echo json_connect($result);
    }
    else {
        echo 'invalid';
    }
} else {
    echo 'invalid';
}

//$url = 'http://leo.rp.edu.sg//workspace/studentModule.asp?site=3&disp=';
//$url = 'http://leo.rp.edu.sg//workspace/studentModule.asp';
//$url = 'http://leo.rp.edu.sg//workspace/student.asp';

?>

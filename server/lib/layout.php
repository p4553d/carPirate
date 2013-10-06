<?php
/**
 * @author Andrej S <p4553d@googlemail.com>
 */
function maketable($title, $head, $data){
	$result = "<table cellspacing=\"0\" class=\"contenttable\"><tr class=\"tabtitle\"><td colspan=\"".count($head)."\">$title</td></tr><tr class=\"tabhead\">";
	foreach ($head as $col) {
		$result .= "<td>$col</td>";
	}
	$result .= "</tr>";

	$i = 1;
	foreach ($data as $row) {
		$result .= "<tr class=\"";
		$result .= ($i++ %2)? "lightgrey": "darkgrey";
		$result .= " hover\">";
		foreach ($row as $cell) {
			$result .= "<td>$cell</td>";
		}
		$result .= "</tr>";

	}

	$result .= "<tr class=\"";
	$result .= ($i++ %2)? "lightgrey": "darkgrey";
	$result .= "\">";
	$result .="<td colspan=\"".count($head)."\">&nbsp;</td>";
	$result .= "</tr>";

	$result .="</table>";

	return $result;
}
?>
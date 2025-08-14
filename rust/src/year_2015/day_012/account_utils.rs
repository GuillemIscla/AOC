use regex::Regex;
use serde_json::Value;
use std::io::{self, Error, ErrorKind};

pub fn find_numbers(s: String) -> Vec<isize> {
    let regex_raw = "[\\-]{0,1}[0-9]+";
    let regex = Regex::new(regex_raw).expect("Regex was wrong");
    regex
        .find_iter(&s)
        .map(|mat| {
            mat.as_str()
                .parse::<isize>()
                .map_err(|e| Error::new(ErrorKind::InvalidData, e))
        })
        .collect::<io::Result<Vec<isize>>>()
        .expect("Error parsing the match into number")
}

pub fn find_numbers_exclude_red(s: String) -> Vec<isize> {
    let serde_value = serde_json::from_str(&s).expect("Could not parse the input into Json");
    find_numbers_exclude_red_internal(&serde_value)
}

fn find_numbers_exclude_red_internal(serde_value: &Value) -> Vec<isize> {
    match serde_value {
        Value::Object(map) => {
            let object_has_red = map
                .values()
                .any(|value| *value == Value::String(String::from("red")));
            if object_has_red {
                Vec::new()
            } else {
                let mut result = Vec::new();
                for (_, val) in map {
                    let mut inner_numbers = find_numbers_exclude_red_internal(val);
                    result.append(&mut inner_numbers);
                }
                result
            }
        }
        Value::Array(array) => {
            let mut result = Vec::new();
            for val in array {
                let mut inner_numbers = find_numbers_exclude_red_internal(val);
                result.append(&mut inner_numbers);
            }
            result
        }
        Value::Number(num) => {
            let num_parsed = num.as_i64().map_or_else(
                || 0,
                |n| isize::try_from(n).expect("Cannot convert number to isize"),
            );
            vec![num_parsed]
        }
        _ => Vec::new(),
    }
}

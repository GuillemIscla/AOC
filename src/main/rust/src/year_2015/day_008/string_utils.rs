use regex::Regex;

pub fn size_in_memory(s: &str) -> usize {
    s.len()
        - 2
        - count_escaped_backslash(0, s)
        - count_escaped_quotes(0, s)
        - (3 * count_escaped_hex(0, s))
}

fn count_escaped_quotes(acc: usize, s: &str) -> usize {
    let mut regex_raw = r"([\\]+)".to_owned();
    let quote = "\"".to_owned();
    regex_raw.push_str(&quote);
    if let Ok(regex) = Regex::new(regex_raw.as_str()) {
        match regex.find(s) {
            Some(regex_match) => {
                let next_index = regex_match.start() + regex_match.len();
                if let Some(captures) = regex.captures(s) {
                    let slash_count = &captures[1].len();
                    let actual_match = if slash_count % 2 == 1 { 1 } else { 0 };
                    count_escaped_quotes(acc + actual_match, &s[next_index..])
                } else {
                    panic!("Matching regex once but not twice")
                }
            }
            None => acc,
        }
    } else {
        panic!("Regex for escaped quotes was wrong!")
    }
}

fn count_escaped_backslash(acc: usize, s: &str) -> usize {
    match s.find(r"\\") {
        Some(find_start) => count_escaped_backslash(acc + 1, &s[(find_start + 2)..]),
        None => acc,
    }
}

fn count_escaped_hex(acc: usize, s: &str) -> usize {
    if let Ok(regex) = Regex::new(r"\\x[0-9,a-f]{2}") {
        match regex.find(s) {
            Some(regex_match) => {
                let next_index = regex_match.start() + 4;
                count_escaped_hex(acc + 1, &s[next_index..])
            }
            None => acc,
        }
    } else {
        panic!("Regex for escaped hex is was wrong!")
    }
}

pub fn escape_input(input: &String) -> String {
    let mut s = String::from("\"");
    let input_replaced = input.replace(r"\", r"\\").replace("\"", "\\\"");
    s.insert_str(0, &input_replaced);
    s.push_str(&String::from("\""));
    s
}

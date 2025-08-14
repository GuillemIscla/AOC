use std::fmt::Display;

#[derive(Eq, PartialEq, Clone, PartialOrd, Ord, Debug, Hash)]
pub struct Element {
    pub raw: String,
}

impl Element {
    pub fn from_raw_to_vec(raw: &str) -> Vec<Element> {
        let mut raw_iter = raw;
        let mut result = Vec::new();

        while !raw_iter.is_empty() {
            let mut chars = raw_iter.chars();
            let first = chars.next().unwrap();
            match chars.next() {
                Some(second) if second.is_lowercase() => {
                    let mut raw = first.to_string();
                    raw.push(second);
                    result.push(Element { raw });
                    raw_iter = &raw_iter[2..];
                }
                Some(_) => {
                    let raw = first.to_string();
                    result.push(Element { raw });
                    raw_iter = &raw_iter[1..];
                }
                None => {
                    let raw = first.to_string();
                    result.push(Element { raw });
                    raw_iter = &raw_iter[1..];
                }
            }
        }
        result
    }
    pub fn from_raw(raw: &str) -> Element {
        let raw = String::from(raw);
        Element { raw }
    }
}

impl Display for Element {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> Result<(), std::fmt::Error> {
        write!(f, "{}", self.raw)
    }
}

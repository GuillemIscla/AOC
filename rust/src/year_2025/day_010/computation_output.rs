pub enum ComputationOutput<T> {
    Computed(T),
    NotComputed,
    Error(String),
}

impl<T> From<String> for ComputationOutput<T> {
    fn from(v: String) -> Self {
        Self::Error(v)
    }
}

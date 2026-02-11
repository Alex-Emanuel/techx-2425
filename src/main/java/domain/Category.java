package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    DEVELOPMENT("Development & Programming 💻", "Development & Programming"),
    CLOUD_INFRASTRUCTURE("Cloud & Infrastructure ☁️", "Cloud & Infrastructure"),
    CYBERSECURITY("Cybersecurity 🔒", "Cybersecurity"),
    AI_ML("AI & Machine Learning 🤖", "AI & Machine Learning"),
    DATA_ANALYTICS("Data & Analytics 📊", "Data & Analytics"),
    IT_MANAGEMENT("IT Management & Strategy 🧑‍💼", "IT Management & Strategy"),
    DIVERSITY_IN_TECH("Diversity in Tech 🌍", "Diversity in Tech");

    private final String displayNameWithEmoji;
    private final String displayNameWithoutEmoji;
}

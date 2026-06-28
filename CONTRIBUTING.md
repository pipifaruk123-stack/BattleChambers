# BattleChambers Contributing Guide

Thank you for your interest in contributing to BattleChambers! This document provides guidelines and instructions for contributing.

## Code of Conduct

Please be respectful and constructive in all interactions.

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/YOUR_USERNAME/BattleChambers.git`
3. Create a branch: `git checkout -b feature/your-feature-name`
4. Make changes and commit: `git commit -am 'Add feature'`
5. Push to branch: `git push origin feature/your-feature-name`
6. Submit a Pull Request

## Development Setup

```bash
# Clone repository
git clone https://github.com/pipifaruk123-stack/BattleChambers.git
cd BattleChambers

# Build project
mvn clean package

# Run tests
mvn test
```

## Coding Standards

- Follow Java naming conventions
- Use descriptive variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and single-responsibility
- Use try-catch blocks appropriately

## Adding a New Mini-Game

1. Create a new class in `src/main/java/com/battlechambers/games/`
2. Extend `AbstractMiniGame`
3. Implement required methods
4. Register in `GameManager`
5. Add configuration in `games.yml`
6. Write tests

## Commit Messages

Use clear, descriptive commit messages:
- `Add: new feature`
- `Fix: bug description`
- `Update: documentation`
- `Refactor: code section`

## Testing

All code should include tests:
```bash
mvn test
```

## Documentation

- Update README.md if adding features
- Add JavaDoc comments
- Update configuration examples

## Pull Request Process

1. Ensure code builds: `mvn clean package`
2. Add/update tests
3. Update documentation
4. Describe changes clearly in PR
5. Link related issues

## Questions?

Open an issue or start a discussion on GitHub!

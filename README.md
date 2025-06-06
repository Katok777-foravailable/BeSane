# BeSane

**BeSane** — это плагин для Minecraft, который добавляет новый элемент геймплея, связанный с высотой. Если игрок опускается ниже заданной высоты (N-ной высоты блока), его рассудок начинает постепенно уменьшаться. Это создает дополнительный вызов и стимулирует игроков избегать низких уровней мира.

## Основные особенности:
- Настраиваемая минимальная высота, ниже которой начинает снижаться рассудок.
- Постепенное уменьшение рассудка при нахождении ниже установленной высоты.
- Возможность интеграции с другими плагинами для создания уникального игрового опыта.

## При низком рассудке выдаются эффекты настраиваемые в конфигурации плагина

## Установка:
1. Скачайте плагин и поместите его в папку `plugins` вашего сервера.
2. Перезапустите сервер.
3. Настройте минимальную высоту в конфигурационном файле.

## Использование:
- Убедитесь, что плагин активирован.
- Установите минимальную высоту в конфигурации.
- Наслаждайтесь новым уровнем сложности в игре!

# Placeholders:
- %besane_sanity% - показывает текущий уровень рассудка игрока в процентах.

# Commands:
- /besane - показывает информацию о плагине и его настройках.
- /besane reload - перезагружает конфигурацию плагина.
- /besane setheight <height> - устанавливает минимальную высоту для снижения рассудка.
- /besane sanity <player> - показывает уровень рассудка указанного игрока.
- /besane sanity set <player> <value> - устанавливает уровень рассудка указанного игрока.

# Permissions:
- besane.admin - доступ к командам администратора.

# Дополнительно
При перезапуске сервера, сбрасывает рассудок до нормального значения

Этот плагин идеально подходит для серверов, которые хотят добавить элемент выживания и стратегического планирования.
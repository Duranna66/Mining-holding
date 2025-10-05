-- Insert enterprises
INSERT INTO enterprises (name, type, location, created_at, updated_at) VALUES
('Норильский горно-металлургический комбинат', 'MINING', 'Норильск', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Кольская горно-металлургическая компания', 'MINING', 'Мурманская область', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Удоканский ГОК', 'MINING', 'Забайкальский край', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Медный завод', 'PROCESSING', 'Санкт-Петербург', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Никелевый завод', 'PROCESSING', 'Мончегорск', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Управляющая компания', 'OFFICE', 'Санкт-Петербург', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert departments for enterprise 1 (Норильский ГМК)
INSERT INTO departments (enterprise_id, name, type, created_at, updated_at) VALUES
(1, 'Таймырский рудник', 'MINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Октябрьский рудник', 'MINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Механическая мастерская', 'WORKSHOP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Служба ремонта оборудования', 'REPAIR_SERVICE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Центральный склад', 'WAREHOUSE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert departments for enterprise 2 (Кольская ГМК)
INSERT INTO departments (enterprise_id, name, type, created_at, updated_at) VALUES
(2, 'Подземный рудник', 'MINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Обогатительная фабрика', 'WORKSHOP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Ремонтно-механический цех', 'REPAIR_SERVICE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Материальный склад', 'WAREHOUSE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert departments for enterprise 3 (Удоканский ГОК)
INSERT INTO departments (enterprise_id, name, type, created_at, updated_at) VALUES
(3, 'Карьер №1', 'MINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Дробильно-сортировочный комплекс', 'WORKSHOP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Служба главного механика', 'REPAIR_SERVICE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Склад ГСМ', 'WAREHOUSE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert departments for enterprise 4 (Медный завод)
INSERT INTO departments (enterprise_id, name, type, created_at, updated_at) VALUES
(4, 'Плавильный цех', 'WORKSHOP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Цех рафинирования', 'WORKSHOP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Ремонтная служба', 'REPAIR_SERVICE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Склад химических реагентов', 'WAREHOUSE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert departments for enterprise 5 (Никелевый завод)
INSERT INTO departments (enterprise_id, name, type, created_at, updated_at) VALUES
(5, 'Плавильное отделение', 'WORKSHOP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Электролизный цех', 'WORKSHOP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Ремонтно-строительный участок', 'REPAIR_SERVICE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert departments for enterprise 6 (Управляющая компания)
INSERT INTO departments (enterprise_id, name, type, created_at, updated_at) VALUES
(6, 'Финансовый отдел', 'FINANCE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Отдел кадров', 'HR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Административный отдел', 'ADMINISTRATION', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert materials
INSERT INTO materials (name, category, unit, description, created_at, updated_at) VALUES
('Медная руда', 'RAW_MATERIAL', 'тонна', 'Сырая медная руда для переработки', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Никелевая руда', 'RAW_MATERIAL', 'тонна', 'Сырая никелевая руда для переработки', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Концентрат меди', 'RAW_MATERIAL', 'тонна', 'Обогащенный медный концентрат', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Концентрат никеля', 'RAW_MATERIAL', 'тонна', 'Обогащенный никелевый концентрат', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Кобальтовое сырье', 'RAW_MATERIAL', 'тонна', 'Кобальтосодержащее сырье', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Подшипник качения', 'SPARE_PART', 'шт', 'Подшипник для горного оборудования', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Гидравлический фильтр', 'SPARE_PART', 'шт', 'Фильтр для гидравлической системы', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Конвейерная лента', 'SPARE_PART', 'метр', 'Резинотканевая конвейерная лента', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Зубья ковша экскаватора', 'SPARE_PART', 'шт', 'Сменные зубья для ковша', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Электродвигатель', 'SPARE_PART', 'шт', 'Асинхронный электродвигатель', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Серная кислота', 'REAGENT', 'тонна', 'Концентрированная серная кислота', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Известняк', 'REAGENT', 'тонна', 'Известняк для металлургии', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Флотационный реагент', 'REAGENT', 'литр', 'Реагент для обогащения руды', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Активированный уголь', 'REAGENT', 'кг', 'Сорбент для очистки', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ксантогенат', 'REAGENT', 'кг', 'Флотационный реагент', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Дизельное топливо', 'FUEL', 'литр', 'ДТ для горной техники', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Бензин АИ-95', 'FUEL', 'литр', 'Автомобильный бензин', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Мазут', 'FUEL', 'тонна', 'Топочный мазут', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Природный газ', 'FUEL', 'м³', 'Природный газ для плавильных печей', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Электроды графитовые', 'FUEL', 'шт', 'Электроды для электропечей', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Спецодежда', 'OTHER', 'комплект', 'Комплект спецодежды для рабочих', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Средства индивидуальной защиты', 'OTHER', 'комплект', 'СИЗ (каски, респираторы, перчатки)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Взрывчатые вещества', 'OTHER', 'кг', 'ВВ для буровзрывных работ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Буровые коронки', 'OTHER', 'шт', 'Коронки для буровых станков', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Анкерная крепь', 'OTHER', 'шт', 'Анкеры для крепления горных выработок', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Огнеупорный кирпич', 'OTHER', 'шт', 'Кирпич для футеровки печей', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Смазочные материалы', 'OTHER', 'литр', 'Индустриальные масла и смазки', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Крепежные изделия', 'OTHER', 'кг', 'Болты, гайки, шайбы', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Кабельная продукция', 'OTHER', 'метр', 'Силовой и контрольный кабель', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Сварочные электроды', 'OTHER', 'кг', 'Электроды для ручной дуговой сварки', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert equipment
INSERT INTO equipment (department_id, name, type, inventory_number, status, created_at, updated_at) VALUES
(1, 'Экскаватор Hitachi EX1200', 'EXCAVATOR', 'EXC-001-2023', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Экскаватор Komatsu PC2000', 'EXCAVATOR', 'EXC-002-2023', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'БелАЗ-75131 грузовик №1', 'TRUCK', 'TRK-001-2022', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'БелАЗ-75131 грузовик №2', 'TRUCK', 'TRK-002-2022', 'UNDER_REPAIR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Экскаватор Caterpillar 6030', 'EXCAVATOR', 'EXC-003-2021', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'БелАЗ-75600 грузовик', 'TRUCK', 'TRK-003-2021', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Конвейер ленточный КЛ-1200', 'CONVEYOR', 'CNV-001-2020', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Конвейер ленточный КЛ-800', 'CONVEYOR', 'CNV-002-2020', 'IDLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Конвейер обогатительный', 'CONVEYOR', 'CNV-003-2019', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Конвейер сортировочный', 'CONVEYOR', 'CNV-004-2022', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'Плавильная печь №1', 'FURNACE', 'FRN-001-2018', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'Плавильная печь №2', 'FURNACE', 'FRN-002-2018', 'UNDER_REPAIR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 'Рафинировочная печь', 'FURNACE', 'FRN-003-2019', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(19, 'Электролизер №1', 'REACTOR', 'RCT-001-2020', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(19, 'Электролизер №2', 'REACTOR', 'RCT-002-2020', 'OPERATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

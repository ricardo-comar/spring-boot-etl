package com.github.ricardocomar.springbootetl.etlconsumer.fixture;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.github.ricardocomar.springbootetl.etlconsumer.model.Purchase;
import com.github.ricardocomar.springbootetl.etlconsumer.model.Purchase.PurchaseStatus;
import com.github.ricardocomar.springbootetl.etlconsumer.model.PurchaseItem;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class PurchaseModelFixture implements TemplateLoader {

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public void load() {
		Fixture.of(Purchase.class).addTemplate("valid", new Rule() {
			{
				add("transaction", "TRANPURC-1");
				add("id", "purc-10");
				add("customer", "Lala Silva");
				add("totalValue", new BigDecimal(500.00 + 3900.00 + 11500.00).setScale(2, RoundingMode.HALF_EVEN));
				add("status", PurchaseStatus.OPEN);
				add("date", LocalDate.now().minusDays(10));
				add("items", has(3).of(PurchaseItem.class, "g6play", "iphone10", "iphone11pro"));
			}
		});
		Fixture.of(Purchase.class).addTemplate("over6", new Rule() {
			{
				add("transaction", "TRANPURC-1");
				add("id", "purc-10");
				add("customer", "Lala Silva");
				add("totalValue",
						new BigDecimal((500.00 + 3900.00 + 11500.00) * 2.0).setScale(2,
						RoundingMode.HALF_EVEN));
				add("status", PurchaseStatus.OPEN);
				add("date", LocalDate.now().minusDays(10));
				add("items", has(6).of(PurchaseItem.class, "g6play", "iphone10", "iphone11pro"));
			}
		});
	}

}

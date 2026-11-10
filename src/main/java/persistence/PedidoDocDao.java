package persistence;

import java.util.List;

import Restaurante_Completo.LeCartoon_Original.Costumer;
import Restaurante_Completo.LeCartoon_Original.Resena;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;

/** Documento del pedido en la base NoSQL (copia de lo que ya está en Oracle). */
public interface PedidoDocDao {
	// crea el documento "pedido::<id>" con el id que dio Oracle
	void crear(int idPedido, Costumer cliente, int idSucursal, List<ItemPedido> items);
	// pone la reseña dentro del documento
	void calificar(int idPedido, Resena resena);
}